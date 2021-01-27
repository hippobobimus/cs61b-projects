public class Body {
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;

    /** Constructor method. */
    public Body(double xP, double yP, double xV,
                    double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    /** Constructor that duplicates instance. */
    public Body(Body b) {
        this.xxPos = b.xxPos;
        this.yyPos = b.yyPos;
        this.xxVel = b.xxVel;
        this.yyVel = b.yyVel;
        this.mass = b.mass;
        this.imgFileName = b.imgFileName;
    }

    /** Calculates the distance between this Body and a given Body. */
    public double calcDistance(Body b) {
        return Math.sqrt(Math.pow(this.xxPos - b.xxPos, 2)
                            + Math.pow(this.yyPos - b.yyPos, 2));
    }

    /** Calculates the force exerted on this Body by a given Body. */
    public double calcForceExertedBy(Body b) {
        if (this.equals(b))
            return 0;
        return 6.67e-11 * this.mass * b.mass
                / Math.pow(this.calcDistance(b), 2);
    }

    /** Calculates the force exerted on this Body in the x direction
     *  by a given Body. */
    public double calcForceExertedByX(Body b) {
        if (this.equals(b))
            return 0;
        return this.calcForceExertedBy(b) * (b.xxPos - this.xxPos)
                / this.calcDistance(b);
    }

    /** Calculates the force exerted on this Body in the y direction
     *  by a given Body. */
    public double calcForceExertedByY(Body b) {
        if (this.equals(b))
            return 0;
        return this.calcForceExertedBy(b) * (b.yyPos - this.yyPos)
                / this.calcDistance(b);
    }

    /** Calculates the net force exerted on this body in the x direction
     *  by a given array of bodies. */
    public double calcNetForceExertedByX(Body[] bodies) {
        double result = 0;
        for (Body b : bodies) {
            if (this.equals(b))
                continue;
            result += this.calcForceExertedByX(b);
        }
        return result;
    }

    /** Calculates the net force exerted on this body in the x direction
     *  by a given array of bodies. */
    public double calcNetForceExertedByY(Body[] bodies) {
        double result = 0;
        for (Body b : bodies) {
            if (this.equals(b))
                continue;
            result += this.calcForceExertedByY(b);
        }
        return result;
    }

    /** Updates the velocity and position of this body given forces in the
     *  x and y direction applied for a time period dt. */
    public void update(double dt, double fX, double fY) {
        double aX = fX / this.mass;
        double aY = fY / this.mass;
        this.xxVel += dt * aX;
        this.yyVel += dt * aY;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }
}
