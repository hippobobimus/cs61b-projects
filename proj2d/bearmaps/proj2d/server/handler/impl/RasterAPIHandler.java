package bearmaps.proj2d.server.handler.impl;

import bearmaps.proj2d.AugmentedStreetMapGraph;
import bearmaps.proj2d.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2d.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2d.utils.Constants.SEMANTIC_STREET_GRAPH;
import static bearmaps.proj2d.utils.Constants.ROUTE_LIST;
import static bearmaps.proj2d.utils.Constants.ROOT_ULLON;
import static bearmaps.proj2d.utils.Constants.ROOT_ULLAT;
import static bearmaps.proj2d.utils.Constants.ROOT_LRLON;
import static bearmaps.proj2d.utils.Constants.ROOT_LRLAT;
import static bearmaps.proj2d.utils.Constants.TILE_SIZE;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {
    private static final int MAX_DEPTH = 7;

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        System.out.println("Parameters given by the web browser:");
        System.out.println(requestParams);

        MapRaster raster = new MapRaster(requestParams);
        System.out.println(raster);

        return raster.results();
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                //System.out.println("Getting image: " + Constants.IMG_ROOT + renderGrid[r][c]);
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }

    /** HELPER CLASSES */

    private class MapRaster {
        private QueryBox qb;
        private int depth;
        private double ullon, lrlon, ullat, lrlat;
        private String[][] renderGrid;
        private boolean querySuccess;
        private Map<String, Object> results;

        private int minX, minY, maxX, maxY, xTiles, yTiles;

        public MapRaster(Map<String, Double> requestParams) {
            qb = new QueryBox(requestParams);
            querySuccess = validateQueryBox();

            depth = calcDepth();

            // Tile indices.
            minX = xIndex(qb.ullon(), depth);
            maxX = xIndex(qb.lrlon(), depth);
            minY = yIndex(qb.ullat(), depth);
            maxY = yIndex(qb.lrlat(), depth);
            xTiles = maxX - minX + 1;
            yTiles = maxY - minY + 1;

            // Coords.
            ullon = calcUllon();
            lrlon = calcLrlon();
            ullat = calcUllat();
            lrlat = calcLrlat();

            // Image filenames.
            renderGrid = generateRenderGrid();

            results = generateResults();
        }

        public int depth() {
            return depth;
        }
        public double ullon() {
            return ullon;
        }
        public double ullat() {
            return ullat;
        }
        public double lrlon() {
            return lrlon;
        }
        public double lrlat() {
            return lrlat;
        }
        public String[][] renderGrid() {
            return renderGrid;
        }
        public boolean querySuccess() {
            return querySuccess;
        }
        public Map<String, Object> results() {
            return results;
        }

        @Override
        public String toString() {
            String s = "RASTER:\n" +
                ">\tdepth = " + depth() + "\n" +
                ">\tullon = " + ullon() + "\n" +
                ">\tullat = " + ullat() + "\n" +
                ">\tlrlon = " + lrlon() + "\n" +
                ">\tlrlat = " + lrlat() + "\n" +
                ">\trenderGrid = " + renderGrid() + "\n" +
                ">\tquerySuccess = " + querySuccess();
            return s;
        }

        /** HELPER FUNCTIONS */

        /** Calculates the longitudinal distance per pixel given the width in 
          * pixels and the upper-left and lower-right longitudinal coords. */
        private double calcLonDPP(double ullon, double lrlon, double width) {
            return (lrlon - ullon) / width;
        }

        /** Calculates the depth of the raster between 0 and MAX_DEPTH given the
          * longitudinal distance per pixel of the query box. */
        private int calcDepth() {
            int d;
            double lonDPPImg, widthInPx;
            for (d = 0; d < MAX_DEPTH; d++) {
                widthInPx = TILE_SIZE * Math.pow(2, d);
                lonDPPImg = calcLonDPP(ROOT_ULLON, ROOT_LRLON, widthInPx);
                if (lonDPPImg <= qb.lonDPP()) {
                    break;
                }
            }
            return d;
        }

        /** */
        private double calcUllon() {
            int indent = minX;
            return calcCoord(ROOT_ULLON, ROOT_LRLON, indent);
        }
        /** */
        private double calcLrlon() {
            int indent = maxX + 1;
            return calcCoord(ROOT_ULLON, ROOT_LRLON, indent);
        }
        /** */
        private double calcUllat() {
            int indent = minY;
            return calcCoord(ROOT_ULLAT, ROOT_LRLAT, indent);
        }
        /** */
        private double calcLrlat() {
            int indent = maxY + 1;
            return calcCoord(ROOT_ULLAT, ROOT_LRLAT, indent);
        }
        /** Calculates a tile corner coordinate given the upper-left and 
          * lower-right root coordinates and the number of tile lengths that
          * the corner is from the upper-left root. */
        private double calcCoord(double rootUL, double rootLR, int tileIndent) {
            double mapLength = rootLR - rootUL;
            double tileLength = mapLength / Math.pow(2, depth());
            double coord = rootUL + tileIndent * tileLength;
            return coord;
        }

        /** Generate a 2d array of map image filenames ordered by x and y
          * indices. */
        private String[][] generateRenderGrid() {
            String[][] render_grid = new String[yTiles][xTiles];
            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    String img = "d" + depth() + "_x" + x + "_y" + y + ".png";
                    render_grid[y - minY][x - minX] = img;
                }
            }
            return render_grid;
        }

        /** Generates a results Map as required by the specification in
          * processRequests. */
        private Map<String, Object> generateResults() {
            Map<String, Object> results = new HashMap<>();
            results.put("render_grid", renderGrid());
            results.put("raster_ul_lon", ullon());
            results.put("raster_ul_lat", ullat());
            results.put("raster_lr_lon", lrlon());
            results.put("raster_lr_lat", lrlat());
            results.put("depth", depth());
            results.put("query_success", querySuccess());
            return results;
        }

        /** Calculates the maximum x (longitudinal) or y (latitudinal) tile
          * index at a given depth. */
        private int maxIndex(int depth) {
            return (int) Math.pow(2, depth) - 1;
        }

        /** Calculates the x (longitudinal) tile index between 0 and 2^depth -1
          * in which the given longitudinal coordinate resides. If the
          * coordinate is out of bounds then the index of the nearest tile is
          * given. */
        private int xIndex(double lon, int depth) {
            return tileIndex(lon, ROOT_ULLON, ROOT_LRLON, depth);
        }

        /** Calculates the y (latitudinal) tile index between 0 and 2^depth -1
          * in which the given latitudinal coordinate resides. If the
          * coordinate is out of bounds then the index of the nearest tile is
          * given. */
        private int yIndex(double lat, int depth) {
            return tileIndex(lat, ROOT_ULLAT, ROOT_LRLAT, depth);
        }

        /** Calculates the tile index in which a coordinate falls given its
          * position relative to the upper left corner of the raster area.
          * A coordinate outside of the raster area returns the nearest tile
          * index. */
        private int tileIndex(double coord, double rootUL, double rootLR, int depth) {
            if (Math.abs(coord) > Math.abs(rootUL)) {
                return 0;
            }
            if (Math.abs(coord) < Math.abs(rootLR)) {
                return maxIndex(depth);
            }
            double rasterSize = Math.abs(rootLR - rootUL);
            double tileSize = rasterSize / Math.pow(2, depth);
            double coordDist = Math.abs(coord - rootUL);
            int result = (int) (coordDist / tileSize); // floor div.
            return result;
        }

        /** Checks that the QueryBox has valid dimensions and position. */
        private boolean validateQueryBox() {
            // Invalid query box.
            if (qb.ullon() > qb.lrlon() || qb.ullat() < qb.lrlat() ||
                    qb.width() <= 0 || qb.height() <= 0) {
                return false;
            }
            // Query box completely outside map area.
            if (qb.lrlon() < ROOT_ULLON || qb.ullon() > ROOT_LRLON ||
                    qb.lrlat() > ROOT_ULLAT || qb.ullat() < ROOT_LRLAT) {
                return false;
            }
            return true;
        }

        private class QueryBox {
            private double ullon, lrlon, ullat, lrlat, width, height;

            QueryBox(Map<String, Double> requestParams) {
                ullon = requestParams.get("ullon");
                lrlon = requestParams.get("lrlon");
                ullat = requestParams.get("ullat");
                lrlat = requestParams.get("lrlat");
                width = requestParams.get("w");
                height = requestParams.get("h");
            }

            private double ullon() {
                return ullon;
            }
            private double lrlon() {
                return lrlon;
            }
            private double ullat() {
                return ullat;
            }
            private double lrlat() {
                return lrlat;
            }
            private double width() {
                return width;
            }
            private double height() {
                return height;
            }
            private double lonDPP() {
                return calcLonDPP(ullon, lrlon, width);
            }
        }
    }
}
