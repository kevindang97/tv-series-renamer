package kevindang97.tvSeriesRenamer.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class only has static methods, so it is declared final with a private constructor.
 * 
 * @author Kevin
 */
public final class HttpClient {

  private final String rootUrl = "https://api.thetvdb.com";
  private final JSONParser jsonParser = new JSONParser();
  private final SeriesRenamer seriesRenamer;

  private String token = "";

  public HttpClient(SeriesRenamer seriesRenamer) {
    this.seriesRenamer = seriesRenamer;
  }

  /**
   * Returns a list of episode names from the given series and season. This list is sorted by
   * episode airing date.
   * 
   * @param seriesName
   * @param seasonNumber
   * @return
   * @throws Exception
   */
  public List<String> getEpisodeNames(String seriesName, int seasonNumber) throws Exception {
    JSONObject seriesInfo = getSeriesInfo(seriesName);
    long seriesId = (long) seriesInfo.get("id");
    return getEpisodeNames(seriesId, seasonNumber);
  }

  private List<String> getEpisodeNames(long seriesId, int seasonNumber) throws Exception {

    /**
     * Method local class used to sort episode names by airing date
     * 
     * @author Kevin
     */
    class Episode implements Comparable<Episode> {
      public final int episodeNumber;
      public final String episodeName;

      public Episode(int episodeNumber, String episodeName) {
        this.episodeNumber = episodeNumber;
        this.episodeName = episodeName;
      }

      @Override
      public int compareTo(Episode other) {
        return Integer.compare(this.episodeNumber, other.episodeNumber);
      }
    }


    // Send query for specific seriesId and season number.
    String url = rootUrl + "/series/" + seriesId + "/episodes/query?airedSeason=" + seasonNumber;
    JSONObject response = sendGet(url);

    JSONArray data = (JSONArray) response.get("data");

    // Extract episode names and episode airing order from query results.
    List<Episode> episodeList = new ArrayList<Episode>();

    for (int i = 0; i < data.size(); i++) {
      JSONObject item = (JSONObject) data.get(i);

      long episodeNumber = (long) item.get("airedEpisodeNumber");
      String episodeName = (String) item.get("episodeName");
      episodeList.add(new Episode((int) episodeNumber, episodeName));
    }

    // Sort extracted results by episode airing order.
    Collections.sort(episodeList);

    // Extract only the episode names from the sorted list to return.
    List<String> episodeNameList = new ArrayList<String>();
    for (Episode episode : episodeList) {
      episodeNameList.add(episode.episodeName);
    }

    return episodeNameList;
  }

  private JSONObject getSeriesInfo(String seriesName) throws Exception {
    String url = rootUrl + "/search/series?name=" + URLEncoder.encode(seriesName, "UTF-8");

    // Return the first result from the search
    JSONObject response = sendGet(url);
    JSONArray data = (JSONArray) response.get("data");
    JSONObject firstResult = (JSONObject) data.get(0);
    return firstResult;
  }

  /**
   * Authenticates to the TVDB server by sending my login credentials and receiving a JWT token that
   * is to be placed in the header of any future requests.
   * 
   * @throws Exception
   */
  private void authenticate() throws Exception {
    if (seriesRenamer.getTvdbUsername().equals("") || seriesRenamer.getTvdbUniqueId().equals("")
        || seriesRenamer.getTvdbApiKey().equals("")) {
      throw new Exception("TVDB credentials have not been set");
    }

    URL urlObject = new URL(rootUrl + "/login");
    HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");

    JSONObject jsonBody = new JSONObject();
    jsonBody.put("username", seriesRenamer.getTvdbUsername());
    jsonBody.put("userkey", seriesRenamer.getTvdbUniqueId());
    jsonBody.put("apikey", seriesRenamer.getTvdbApiKey());

    String body = jsonBody.toJSONString();

    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(body);
    wr.close();

    int responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }

    JSONObject jsonResponse = (JSONObject) jsonParser.parse(response.toString());

    token = (String) jsonResponse.get("token");
  }

  private JSONObject sendGet(String url) throws Exception {
    if (token.equals("")) {
      authenticate();
    }

    URL urlObject = new URL(url);
    HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Accept", "application/json");
    con.setRequestProperty("Authorization", "Bearer " + token);

    int responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }

    JSONObject jsonResponse = (JSONObject) jsonParser.parse(response.toString());
    return jsonResponse;
  }

}
