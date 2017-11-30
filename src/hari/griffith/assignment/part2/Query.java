package hari.griffith.assignment.part2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static hari.griffith.assignment.part2.AppConstants.DELIMITER;

/**
 *
 * Main Class for Query Processing
 *
 * **/


class Query {

   private static boolean isQueryNumber =true;

    public static void main(String[] args) {
        PreQueryProcessor preQueryProcessor = new PreQueryProcessor();
        QueryProcessor queryProcessor = new QueryProcessor();
        Utils utils = Utils.getUtilsInstance();

        utils.writeToFile("Query Number"+DELIMITER+"Cosine Similarity Score"+DELIMITER+"Document Number"+DELIMITER+"Document Title");

        //Read from index data generated in indexing part.
        IndexData indexData = preQueryProcessor.preProcess();
        String path = args[0];
        StringBuilder queryBuilder = new StringBuilder();
        boolean isFirst = true;

        //Read List of Queries
        final QueryObject[] query = {null};

        //Read the file
        try(Stream<String> pathStream = Files.lines(Paths.get(path))){
            //Parse through each line
            pathStream.forEach((String line) ->{
                //Avoid leading and trailing spaces..
                line = line.trim();

                //Check if its first line., to read query number, later used to print info.
                if(isQueryNumber){
                    query[0] = new QueryObject();
                    query[0].setQueryNumber(Integer.parseInt(line.trim()));
                    isQueryNumber = false;
                    return;
                }
                //Checks for end of query to reset params and process this query before starting with next query.
                if(line.endsWith("##")){
                    isQueryNumber = true;
                    queryBuilder.append(line.substring(0,line.length()-2));
                    query[0].setQueryText(queryBuilder.toString());

                    //Process the current Query
                    queryProcessor.processQuery(indexData,query[0]);
                    queryBuilder.setLength(0);
                }

                //Read in all the lines till end.
                if(!isQueryNumber) {
                    queryBuilder.append(line);
                    queryBuilder.append(" ");
                }
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //Close file resources

        utils.closeWriter();
    }

}
