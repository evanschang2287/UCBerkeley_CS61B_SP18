public class NBody {
    public static double readRadius(String filename) {
        In rawData = new In(filename);

        int N = rawData.readInt();
        double radiusOfUniverse = rawData.readDouble();

        return radiusOfUniverse;
    }

    public static Planet[] readPlanets(String filename) {
        In rawData = new In(filename);

        int N = rawData.readInt();
        double radiusOfUniverse = rawData.readDouble();

        Planet[] planets = new Planet[N];
        for (int i = 0; i < N; i++) {
            double xPos = Double.parseDouble(rawData.readString());
            double yPos = Double.parseDouble(rawData.readString());
            double xVel = Double.parseDouble(rawData.readString());
            double yVel = Double.parseDouble(rawData.readString());
            double mass = Double.parseDouble(rawData.readString());
            String img = rawData.readString();

            planets[i] = new Planet(xPos, yPos, xVel, yVel, mass, img);
        }

        return planets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = NBody.readPlanets(filename);
        double radiusOfUniverse = NBody.readRadius(filename);

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-1*radiusOfUniverse, radiusOfUniverse);
        StdDraw.picture(0, 0, "images/starfield.jpg");

        for (double time = 0; time <= T; time += dt) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");

            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radiusOfUniverse);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}