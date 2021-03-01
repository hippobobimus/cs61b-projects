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
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
        System.out.println("Since you haven't implemented RasterAPIHandler.processRequest, nothing is displayed in "
                + "your browser.");

        boolean querySuccess = validateRequest(requestParams);
        // Query box...
        double QB_ullon = requestParams.get("ullon");
        double QB_lrlon = requestParams.get("lrlon");
        double QB_ullat = requestParams.get("ullat");
        double QB_lrlat = requestParams.get("lrlat");
        double QB_width = requestParams.get("w");

        double lonDPPQueryBox = lonDPP(QB_ullon, QB_lrlon, QB_width);
        int depth = depth(lonDPPQueryBox);

        System.out.println("DEPTH: " + depth);
        System.out.println("TESTING...");

        // Tile indices.
        int minX = lonTileIdx(QB_ullon, depth);
        int maxX = lonTileIdx(QB_lrlon, depth);
        int minY = latTileIdx(QB_ullat, depth);
        int maxY = latTileIdx(QB_lrlat, depth);

        System.out.println("Lon: " + minX + " to " + maxX);
        System.out.println("Lat: " + minY + " to " + maxY);

        int xTiles = maxX - minX + 1;
        int yTiles = maxY - minY + 1;
        String[][] render_grid = new String[yTiles][xTiles];
        //String[][] render_grid = new String[][]{
        //    {"d2_x0_y1.png", "d2_x1_y1.png", "d2_x2_y1.png", "d2_x3_y1.png"},
        //    {"d2_x0_y2.png", "d2_x1_y2.png", "d2_x2_y2.png", "d2_x3_y2.png"},
        //    {"d2_x0_y3.png", "d2_x1_y3.png", "d2_x2_y3.png", "d2_x3_y3.png"}
        //};

        int x, y;
        for (int i = 0; i < yTiles; i++) {
            y = minY + i;
            for (int j = 0; j < xTiles; j++) {
                x = minX + j;
                String img = "d" + depth + "_x" + x + "_y" + y + ".png";
                System.out.println(img);
                render_grid[i][j] = img;
            }
        }

        System.out.println(render_grid);

        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", -122.2998046875);
        results.put("raster_ul_lat", 37.87484726881516);
        results.put("raster_lr_lon", -122.2119140625);
        results.put("raster_lr_lat", 37.82280243352756);
        results.put("depth", depth);
        results.put("query_success", querySuccess);

        return results;
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
                System.out.println("Getting image: " + Constants.IMG_ROOT + renderGrid[r][c]);
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

    /** */
    private double lonDPP(double ullon, double lrlon, double width) {
        return (lrlon - ullon) / width;
    }

    /** */
    private int depth(double lonDPPQueryBox) {
        int d;
        double lonDPPImg, width;

        for (d = 0; d <= MAX_DEPTH; d++) {
            width = TILE_SIZE * Math.pow(2, d);
            lonDPPImg = lonDPP(ROOT_ULLON, ROOT_LRLON, width);
            if (lonDPPImg <= lonDPPQueryBox) {
                break;
            }
        }

        return d;
    }

    /** Calculates the longitudinal tile index between 0 and 2^depth -1 in which
      * the given longitudinal coordinate resides. If the coordinate is out of
      * bounds then the index of the nearest tile is given. */
    private int lonTileIdx(double lon, int depth) {
        if (lon < ROOT_ULLON) {
            return 0;
        }
        if (lon > ROOT_LRLON) {
            return (int) Math.pow(2, depth) - 1;
        }
        double lonTileWidth = Math.abs((ROOT_LRLON - ROOT_ULLON) / Math.pow(2, depth));
        int result = (int) (Math.abs(lon - ROOT_ULLON) / lonTileWidth); // floor div.
        return result;
    }

    /** Calculates the latitudinal tile index between 0 and 2^depth -1 in which
      * the given latitudinal coordinate resides. If the coordinate is out of
      * bounds then the index of the nearest tile is given. */
    private int latTileIdx(double lat, int depth) {
        if (lat > ROOT_ULLAT) {
            return 0;
        }
        if (lat < ROOT_LRLAT) {
            return (int) Math.pow(2, depth) - 1;
        }
        double latTileHeight = Math.abs((ROOT_LRLAT - ROOT_ULLAT) / Math.pow(2, depth));
        int result = (int) (Math.abs(lat - ROOT_ULLAT) / latTileHeight); // floor div.
        return result;
    }

    /** */
    private boolean validateRequest(Map<String, Double> requestParams) {
        double ullon = requestParams.get("ullon");
        double lrlon = requestParams.get("lrlon");
        double ullat = requestParams.get("ullat");
        double lrlat = requestParams.get("lrlat");
        double width = requestParams.get("w");
        double height = requestParams.get("h");

        // Invalid query box
        if (ullon > lrlon || ullat < lrlat) {
            return false;
        }
        // Query box completely outside map area.
        if (lrlon < ROOT_ULLON || ullon > ROOT_LRLON || lrlat > ROOT_ULLAT || ullat < ROOT_LRLAT) {
            return false;
        }
        return true;
    }
}
