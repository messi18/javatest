import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by malance on 14-9-3.
 */
public abstract class AbstractNAV {
    public static Map<String,Integer> readTickers() throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader("stokcs.txt "));
        final Map<String,Integer> stocks = new HashMap<>();

        String stockInfo = null;
        while ((stockInfo = reader.readLine())!= null){
            final String[] stockInfoData = stockInfo.split(",");
            final String stockTicker = stockInfoData[0];
            final Integer quantity = Integer.valueOf(stockInfoData[1]);
            stocks.put(stockTicker,quantity);
        }

        return stocks;
    }

    public void timeAndComputeValue() throws Exception {
        final long start = System.nanoTime();

        final Map<String, Integer> stocks = readTickers();
        final double nav = computeNetAssetValue(stocks);
        final long end = System.nanoTime();

        final String value = new DecimalFormat("$##,##0.00").format(nav);
        System.out.println(String.format("Your net asset value is {0}",value));
        System.out.println(String.format("Time (Seconds) taken {0}",(end-start)/1.0e9));
    }

    protected abstract double computeNetAssetValue(Map<String, Integer> stocks) throws Exception;
}
