public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double g = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double distance = Math.sqrt((xxPos-p.xxPos)*(xxPos-p.xxPos) + (yyPos-p.yyPos)*(yyPos-p.yyPos));

        return distance;
    }

    public double calcForceExertedBy(Planet p) {
        double distance = calcDistance(p);
        double force = g * mass * p.mass / (distance * distance);

        return force;
    }

    public double calcForceExertedByX(Planet p) {
        double totalForce = calcForceExertedBy(p);
        double dX = p.xxPos - xxPos;
        double distance = calcDistance(p);
        double forceX = totalForce * dX / distance;

        return forceX;
    }

    public double calcForceExertedByY(Planet p) {
        double totalForce = calcForceExertedBy(p);
        double dY = p.yyPos - yyPos;
        double distance = calcDistance(p);
        double forceY = totalForce * dY / distance;

        return forceY;
    }

    public double calcNetForceExertedByX(Planet[] p) {
        double netForceX = 0.0;
        for (Planet testP : p) {
            if (testP.equals(this)) continue;
            netForceX += calcForceExertedByX(testP);
        }

        return netForceX;
    }

    public double calcNetForceExertedByY(Planet[] p) {
        double netForceY = 0.0;
        for (Planet testP : p) {
            if (testP.equals(this)) continue;
            netForceY += calcForceExertedByY(testP);
        }

        return netForceY;
    }

    public void update(double dt, double fX, double fY) {
        double acclX = fX / mass;
        double acclY = fY / mass;
        xxVel = xxVel + acclX * dt;
        yyVel = yyVel + acclY * dt;
        xxPos = xxPos + xxVel * dt;
        yyPos = yyPos + yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
    }
}