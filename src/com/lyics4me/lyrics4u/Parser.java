package com.lyics4me.lyrics4u;

import java.net.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.parser.*;

public abstract class Parser {
    public static String ARTIST = "";
    public static String SONG = "";

    /**
     * Gets a search page from user input request
     * @param input - user input of Song and/or artist
     * @return - search result url
     */
    public static String getSearchURL(String input) {
        return ("http://search.azlyrics.com/search.php?q=" + input.replaceAll(
                " ", "+"));

    }
/**
 * Fetches the URL to the lyric page
 * @param inputURL - URL of the search results page
 * @return - String url of the lyrics page
 * @throws CustomException - No lyrics were found
 */
    public static String getURL(String inputURL) throws CustomException {
        String nextLine = "";
        try {
            final URL url = new URL(inputURL);
            final URLConnection urlConn = url.openConnection();
            final BufferedReader buff = new BufferedReader(
                    new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            nextLine = buff.readLine();
            while (nextLine != null) {
                if (nextLine.equals("<div class=\"sen\">")) {
                    nextLine = buff.readLine();
                    nextLine = nextLine.substring(nextLine.indexOf("http"),
                            nextLine.indexOf("html") + 4);
                    return nextLine;

                } else
                    nextLine = buff.readLine();
            }

            throw new CustomException("No lyrics were found.");

        } catch (MalformedURLException e) {
            throw new CustomException("Please check the URL:" + e.toString());
        } catch (IOException e1) {
            throw new CustomException("Can't read from the Internet: "
                    + e1.toString());
        }
    }

    /**
     * Gets the lyrics and puts them so a string.
     * @param inputURL - URL to the lyrics page
     * @return - String containing the lyrics
     */
    public static String parseLyrics(String inputURL) {
        String nextLine, fullLyrics = "";

        try {
            final URL url = new URL(inputURL);
            final URLConnection urlConn = url.openConnection();
            final BufferedReader buff = new BufferedReader(
                    new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            nextLine = buff.readLine();
            String lyrics = "";
            // Gathers Artist & Song name
            while (!(nextLine.indexOf("ArtistName = ") != -1)) {
                nextLine = buff.readLine();
            }
            ARTIST = (nextLine.substring(14, nextLine.length() - 2));
            nextLine = buff.readLine();
            SONG = (nextLine.substring(12, nextLine.length() - 2));
            fullLyrics = fullLyrics + "Artist: " + ARTIST + "\n" + "Song: "
                    + SONG + "\n";
            
            // Creates the lyrics string
            while (nextLine.indexOf("<!-- start of lyrics -->") == -1) {
                nextLine = buff.readLine();
            }
            while (lyrics.indexOf("<!-- end of lyrics -->") == -1) {
                lyrics = lyrics.replaceAll("<br />", "").replaceAll("<i>", "")
                        .replaceAll("</i>", "");

                fullLyrics = (fullLyrics + "\n" + lyrics);
                lyrics = buff.readLine();
            }
        } catch (MalformedURLException e) {
            System.out.println("Please check the URL:" + e.toString());
        } catch (IOException e1) {
            System.out.println("Can't read  from the Internet: "
                    + e1.toString());
        }
        return fullLyrics;
    }

    /**
     * Sets up the YouTube player video ID
     * @return - String containing the specific video ID
     * @throws IOException 
     * @throws ParseException 
     */
    public static String getVideo() throws IOException, ParseException {
        String urlString = ("http://gdata.youtube.com/feeds/api/videos?q="+ ARTIST + "+" + SONG + "&alt=json&max-results=1").replaceAll(" ", "+");
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine, data = "";
        while ((inputLine = in.readLine()) != null) {
            data += inputLine + "\n";
        }

        data = data.substring(data.indexOf("http://www.youtube.com/watch?v="));
        data = data.substring(data.indexOf("=")+1, data.indexOf("&"));
        return data;

    }
}
