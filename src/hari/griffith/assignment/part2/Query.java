package hari.griffith.assignment.part2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Query {

   private static boolean isQueryNumber =true;

    public static void main(String[] args) {
        PreQueryProcessor preQueryProcessor = new PreQueryProcessor();
        QueryProcessor queryProcessor = new QueryProcessor();
        IndexData indexData = preQueryProcessor.preProcess();
        String path = args[0];
        StringBuilder queryBuilder = new StringBuilder();

        final QueryObject[] query = {null};
        try(Stream<String> pathStream = Files.lines(Paths.get(path))){

            pathStream.forEach((String line) ->{
                line = line.trim();
                if(isQueryNumber){
                    query[0] = new QueryObject();
                    query[0].setQueryNumber(Integer.parseInt(line.trim()));
                    isQueryNumber = false;
                    return;
                }
                if(line.endsWith("##")){
                    isQueryNumber = true;
                    queryBuilder.append(line.substring(0,line.length()-2));
                    query[0].setQueryText(queryBuilder.toString());
                    queryProcessor.processQuery(indexData,query[0]);
                    queryBuilder.setLength(0);
                }
                if(!isQueryNumber) {
                    queryBuilder.append(line);
                    queryBuilder.append(" ");
                }
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}
