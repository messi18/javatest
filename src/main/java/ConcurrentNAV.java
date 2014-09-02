import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by malance on 14-9-3.
 */
public class ConcurrentNAV extends AbstractNAV{
    public double computeNetAssetValue(final Map<String,Integer> stocks)  throws Exception {
        final int numberOfCores = Runtime.getRuntime().availableProcessors();
        final double coeffecient = 0.9;

        final int poolSize = (int)(numberOfCores / (1 - coeffecient));

        System.out.println(String.format("Number of Cores available is {0}", numberOfCores));
        System.out.println(String.format("Pool Size is {0}",poolSize));

        final List<Callable<Double>> partitions = new ArrayList<Callable<Double>>();

        for (final String ticker : stocks.keySet()){
            partitions.add(new Callable<Double>(){
                @Override
                public Double call() throws Exception {
                    return stocks.get(ticker) * YahooFinance.getPrice(ticker);
                }
            });
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        final List<Future<Double>> valueOfStocks = executorService.invokeAll(partitions,10000, TimeUnit.SECONDS);

        double netAssertValue = 0.0;
        for (final Future<Double> valueOfStock : valueOfStocks){
            netAssertValue += valueOfStock.get();
        }

        executorService.shutdown();

        return netAssertValue;
    }

    public static void main(String[] args) throws Exception {
        new ConcurrentNAV().timeAndComputeValue();
    }
}
