package com.lyics4me.lyrics4u;

import java.net.*;
import java.io.*;

public abstract class Parser {
    public static String ARTIST = "";
    public static String SONG = "";

    public static String getSearchURL(String input) {
        return ("http://search.azlyrics.com/search.php?q=" + input.replaceAll(
                " ", "+"));

    }

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

    public static String parseLyrics(String inputURL) {
        String nextLine, fullLyrics = "";

        try {
            final URL url = new URL(inputURL);
            final URLConnection urlConn = url.openConnection();
            final BufferedReader buff = new BufferedReader(
                    new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            nextLine = buff.readLine();
            String lyrics = "";
            // Artist & Song
            while (!(nextLine.indexOf("ArtistName = ") != -1)) {
                nextLine = buff.readLine();
            }
            ARTIST = (nextLine.substring(14, nextLine.length() - 2));
            nextLine = buff.readLine();
            SONG = (nextLine.substring(12, nextLine.length() - 2));
            fullLyrics = fullLyrics + "Artist: " + ARTIST + "\n" + "Song: "
                    + SONG + "\n";
            // Lyrics

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

    public static String getVideo() {
        String id = "";
        String nextLine;
        String inputURL = ("https://www.youtube.com/results?q="
                + ARTIST + "+" + SONG + "&sm=3&app=desktop").replaceAll(" ", "+");
        try {
            final URL url = new URL(inputURL);
            final URLConnection urlConn = url.openConnection();
            final BufferedReader buff = new BufferedReader(
                    new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            nextLine = buff.readLine();

            while (!(nextLine.indexOf("<a href=\"/watch?v=") != -1)) {
                System.out.println(nextLine);
                nextLine = buff.readLine();
            }
            nextLine = nextLine.replaceAll(" ", "");

            id = nextLine.substring(17, 28);

        } catch (MalformedURLException e) {
            System.out.println("Please check the URL:" + e.toString());
        } catch (IOException e1) {
            System.out.println("Can't read  from the Internet: "
                    + e1.toString());
        }
        return id;

    }
}