import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

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
        Boolean query_success = true;

        boolean wrongPos = ullon > lrlon || ullat < lrlat;
        if (wrongPos) {
            query_success = false;
        }

        Double rasterULLON = 0.0;
        Double rasterULLAT = 0.0;
        Double rasterLRLON = 0.0;
        Double rasterLRLAT = 0.0;

        int xMin = 0;
        int xMax = 0;
        int yMin = 0;
        int yMax = 0;

        final double lonPerPic = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth); // 0.02197265625
        final double latPerPic = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth); // 0.017348278429199

        xMin = (int) Math.floor(((ullon - MapServer.ROOT_ULLON) / lonPerPic));
        xMax = (int) (Math.pow(2, depth) - 1 - Math.floor((MapServer.ROOT_LRLON - lrlon) / lonPerPic));
        yMin = (int) Math.floor(((MapServer.ROOT_ULLAT - ullat) / latPerPic));
        yMax = (int) (Math.pow(2, depth) - 1 - Math.floor((lrlat - MapServer.ROOT_LRLAT) / latPerPic));

        rasterULLON = MapServer.ROOT_ULLON + lonPerPic * xMin;
        rasterULLAT = MapServer.ROOT_ULLAT - latPerPic * yMin;
        rasterLRLON = MapServer.ROOT_ULLON + lonPerPic * (xMax + 1);
        rasterLRLAT = MapServer.ROOT_ULLAT - latPerPic * (yMax + 1);

        if (ullon < MapServer.ROOT_ULLON) {
            rasterULLON = MapServer.ROOT_ULLON;
            xMin = 0;
        }
        if (lrlon > MapServer.ROOT_LRLON) {
            rasterLRLON = MapServer.ROOT_LRLON;
            xMax = (int) Math.pow(2, depth) - 1;
        }
        if (ullat > MapServer.ROOT_ULLAT) {
            rasterULLAT = MapServer.ROOT_ULLAT;
            yMin = 0;
        }
        if (lrlat < MapServer.ROOT_LRLAT) {
            rasterLRLAT = MapServer.ROOT_LRLAT;
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
        results.put("query_success", query_success);

        return results;
    }

    private Integer computeDepth(Double ullon, Double lrlon, Double width) {
        Integer depth = 0;
        Integer maxDepth = 7;
        Double desiredLonDPP = Math.abs(ullon - lrlon) * 288200 / width;
        Double initLonDPP = Math.abs(MapServer.ROOT_ULLON - MapServer.ROOT_LRLON) * 288200 / MapServer.TILE_SIZE; // 98.94561767578125

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
