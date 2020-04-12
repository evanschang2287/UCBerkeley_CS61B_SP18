import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final Double ROOT_ULLON = MapServer.ROOT_ULLON;
    private static final Double ROOT_ULLAT = MapServer.ROOT_ULLAT;
    private static final Double ROOT_LRLON = MapServer.ROOT_LRLON;
    private static final Double ROOT_LRLAT = MapServer.ROOT_LRLAT;

    public Rasterer() {
        // YOUR CODE HERE
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
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "rasterULLON" : Number, the bounding upper left longitude of the rastered image. <br>
     * "rasterULLAT" : Number, the bounding upper left latitude of the rastered image. <br>
     * "rasterLRLON" : Number, the bounding lower right longitude of the rastered image. <br>
     * "rasterLRLAT" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();

        String[][] renderGrid;
        Double w = params.get("w");
        Double h = params.get("h");
        Double ullon = params.get("ullon");
        Double ullat = params.get("ullat");
        Double lrlon = params.get("lrlon");
        Double lrlat = params.get("lrlat");
        Integer depth = computeDepth(ullon, lrlon, w);
        Boolean querySuccess = true;

        boolean wrongPos = ullon > lrlon || ullat < lrlat;
        if (wrongPos) {
            querySuccess = false;
        }

        final double lonPerPic = (ROOT_LRLON - ROOT_ULLON) / Math.pow(2, depth);
        final double latPerPic = (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2, depth);

        int xMin = (int) Math.floor(((ullon - ROOT_ULLON) / lonPerPic));
        int xMax = (int) (Math.pow(2, depth) - 1 - Math.floor((ROOT_LRLON - lrlon) / lonPerPic));
        int yMin = (int) Math.floor(((ROOT_ULLAT - ullat) / latPerPic));
        int yMax = (int) (Math.pow(2, depth) - 1 - Math.floor((lrlat - ROOT_LRLAT) / latPerPic));

        Double rasterULLON = ROOT_ULLON + lonPerPic * xMin;
        Double rasterULLAT = ROOT_ULLAT - latPerPic * yMin;
        Double rasterLRLON = ROOT_ULLON + lonPerPic * (xMax + 1);
        Double rasterLRLAT = ROOT_ULLAT - latPerPic * (yMax + 1);

        if (ullon < ROOT_ULLON) {
            rasterULLON = ROOT_ULLON;
            xMin = 0;
        }
        if (lrlon > ROOT_LRLON) {
            rasterLRLON = ROOT_LRLON;
            xMax = (int) Math.pow(2, depth) - 1;
        }
        if (ullat > ROOT_ULLAT) {
            rasterULLAT = ROOT_ULLAT;
            yMin = 0;
        }
        if (lrlat < ROOT_LRLAT) {
            rasterLRLAT = ROOT_LRLAT;
            yMax = (int) Math.pow(2, depth) - 1;
        }

        int xRange = xMax - xMin + 1;
        int yRange = yMax - yMin + 1;
        renderGrid = new String[yRange][xRange];
        for (int j = 0; j < yRange; j++) {
            for (int i = 0; i < xRange; i++) {
                int yNum = yMin + j;
                int xNum = xMin + i;
                renderGrid[j][i] = "d" + depth + "_x" + xNum + "_y" + yNum + ".png";
            }
        }

        results.put("raster_ul_lon", rasterULLON);
        results.put("depth", depth);
        results.put("raster_lr_lon", rasterLRLON);
        results.put("raster_lr_lat", rasterLRLAT);
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lat", rasterULLAT);
        results.put("query_success", querySuccess);

        return results;
    }

    private Integer computeDepth(Double ullon, Double lrlon, Double width) {
        Integer depth = 0;
        Integer maxDepth = 7;
        Double desiredLonDPP = Math.abs(ullon - lrlon) * 288200 / width;
        Double initLonDPP = (ROOT_LRLON - ROOT_ULLON) * 288200 / MapServer.TILE_SIZE;

        while (initLonDPP > desiredLonDPP) {
            if (depth > maxDepth) {
                depth = maxDepth;
                break;
            }
            initLonDPP /= 2;
            depth += 1;
        }
        return depth;
    }
}
