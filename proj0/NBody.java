public class NBody {
    /** Reads and returns the radius of the universe from the specified file. */
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        in.readLine();  // Skip first line.
        return in.readDouble();
    }

    /** Returns an array of bodies read from the named file. */
    public static Body[] readBodies(String fileName) {
        In in = new In(fileName);
        in.readLine();  // Skip first line.
        in.readLine();  // Skip second line.
        Body[] bodies = new Body[5];
        for (int i = 0; i < 5; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String f = in.readString();
            bodies[i] = new Body(xP, yP, xV, yV, m, f);
        }
        return bodies;
    }

    /** Given a time interval, total time and data file location; 
     *  renders and animates a simulation to the screen. */
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String fileName = args[2];
        Body[] bodies = readBodies(fileName);
        double radius = readRadius(fileName);

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();

        double[] xForces = new double[5];
        double[] yForces = new double[5];
        for (double t = 0; t <= T; t += dt) {
            for (int i = 0; i < bodies.length; i++) {
                xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
                yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
            }
		    StdDraw.picture(0, 0, "images/starfield.jpg");
            for (int i = 0; i < bodies.length; i++) {
                bodies[i].update(dt, xForces[i], yForces[i]);
                bodies[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", bodies.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < bodies.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                          bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
        }
    }
}
