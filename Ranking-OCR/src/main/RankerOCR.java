/*
 * Copyright (C) 2014 Nils Ryter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import libraries.PackageClassList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ranking.Ranker;

/**
 *
 * @author Nils Ryter
 */
public class RankerOCR {

    //Show help on the CLI
    private static final HelpFormatter hf = new HelpFormatter();
    //Manage options of CLI
    private static final Options options = new Options();

    static {
        //Adding option
        options.addOption("gui", false, "Launch a graphical user interface");
        options.addOption("decimal", true, "Set the number of decimal after 0");
        options.addOption("origin", true,
                "Set the file name of the original document");
        options.addOption("compared", true,
                "Set the file name of the document to compare");
        options.addOption("ranker", true,
                "Set the ranker used for the comparison");
        options.addOption("help", false, "Show the help");
        //Help formater
        hf.setOptPrefix("-");
    }

    /**
     * Command line interface for RankerOCR.
     * <p>
     * <b>Command line input:</b>
     * <ul>
     * usage: java -jar Ranker-OCR [options]
     * </ul>
     * <b>Options list:</b>
     * <ul>
     * -compared [arg] Set the file name of the document to compare
     * <br>
     * -decimal [arg] Set the number of decimal after 0
     * <br>
     * -gui Launch a graphical user interface
     * <br>
     * -help Show the help
     * <br>
     * -origin [arg] Set the file name of the original document
     * <br>
     * -ranker [arg] Set the ranker used for the comparison
     * </ul>
     * <b>Return values are as follows:</b>
     * <br>
     * Double representation as a integer. The number of decimal after the dot
     * can be set. The result will be like this ##### but you should interpret
     * ###.##
     * <p>
     * <b>Return values are if error:</b>
     * <ul>
     * (-1) The precision parameter is not a number.
     * <br>
     * (-2) The precision parameter is lower than 0.
     * <br>
     * (-3) The precision parameter is greater than 10.
     * <br>
     * (-4) Internal error when converting URL to URI. Please check file name.
     * <br>
     * (-5) Error when access to help file
     * <br>
     * (-6) Error when parsing parameters
     * <br>
     * (-7) File name of the option -origin is not correct
     * <br>
     * (-8) File name of the option -compared is not correct
     * <br>
     * (-9) Error when access to documents files
     * <br>
     * (-10) The ranker name is wrong
     * <br>
     * (-11) Internal error when get the ranker list. Please report as a bug.
     * <br>
     * (-12) Internal error when creating the ranker. Please report as a bug.
     * </ul>
     * <p>
     * @param args Argument array which can have the values from options list
     */
    public static void main(String[] args) {
        //Parse command line
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            //<editor-fold defaultstate="collapsed" desc="Show Help">
            if (cmd.hasOption("help")) {
                showHelp(hf, options);
                System.exit(0);
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Show GUI">
            if ((!cmd.hasOption("origin") || !cmd.hasOption("compared"))
                    && cmd.hasOption("gui")) {
                display.DispalyRankerOCR.main(new String[]{});
                System.exit(0);
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Rank documents">
            if (cmd.hasOption("origin") && cmd.hasOption("compared")) {
                //Check document path and access
                File f1 = new File(cmd.getOptionValue("origin", ""));
                File f2 = new File(cmd.getOptionValue("compared", ""));
                if (!f1.isFile()) {
                    System.exit(-7);
                }
                if (!f2.isFile()) {
                    System.exit(-8);
                }
                //Try to get the content of documents
                try {
                    String s1 = getDocumentText(f1);
                    String s2 = getDocumentText(f2);
                    //Show the document in GUI or compare them in CLI
                    if (cmd.hasOption("gui")) {
                        display.DispalyRankerOCR.main(new String[]{s1, s2});
                    } else if (cmd.hasOption("ranker")) {
                        rankDocuments(s1, s2, cmd.getOptionValue("ranker", ""),
                                convertPrecision(cmd.getOptionValue(
                                                "decimal", "2")));
                    }
                } catch (IOException ex) {
                    System.exit(-9);
                }
            }
            //</editor-fold>
        } catch (ParseException ex) {
            System.exit(-6);
        }
        hf.printUsage(new PrintWriter(System.out, true), 50,
                "java -jar Ranker-OCR [options]");
        System.exit(0);
    }

    /**
     * Convert the precision parameter.
     * <p>
     * @param s precision parameter as a string
     * @return Precision parameter as a integer
     */
    private static int convertPrecision(String s) {
        try {
            int precision;
            precision = Integer.valueOf(s);
            if (precision < 0) {
                System.exit(-2);
            } else if (precision > 10) {
                System.exit(-3);
            }
            return precision;
        } catch (NumberFormatException ex) {
            System.exit(-1);
            return 0;
        }
    }

    /**
     * Extract a file content as a string.
     * <p>
     * @param f Document file
     * @return Text of the document
     */
    private static String getDocumentText(File f) throws IOException {
        FileReader fr;
        fr = new FileReader(f);
        BufferedReader b = new BufferedReader(fr);
        StringBuilder s = new StringBuilder();
        while (true) {
            String line = b.readLine();
            if (line == null) {
                break;
            } else {
                s.append(line);
                s.append("\n");
            }
        }
        return s.toString();
    }

    /**
     * Return a stream content as a string.
     * <p>
     * @param is Input stream
     * @return Text of the document
     */
    private static String getDocumentText(InputStream is) throws IOException {
        InputStreamReader fr;
        fr = new InputStreamReader(is);
        BufferedReader b = new BufferedReader(fr);
        StringBuilder s = new StringBuilder();
        while (true) {
            String line = b.readLine();
            if (line == null) {
                break;
            } else {
                s.append(line);
                s.append("\n");
            }
        }
        return s.toString();
    }

    /**
     * Compare the documents and rank them.
     * <p>
     * @param d1 Origin text
     * @param d2 Comparative text
     * @param ranker Name of the ranker to use
     * @param decimal The number of decimal wanted
     */
    private static void rankDocuments(String d1, String d2, String ranker,
            int decimal) {
        try {
            Class rankerClass = Class.forName("ranking." + ranker);
            Ranker instance = (Ranker) rankerClass.newInstance();
            double rate = instance.compare(d1, d2);
            System.exit((int) (Math.pow(10, decimal) * rate));
        } catch (ClassNotFoundException ex) {
            System.exit(-10);
        } catch (InstantiationException | IllegalAccessException ex) {
            System.exit(-12);
        }
    }

    /**
     * Show the help on CLI.
     * <p>
     * Help is from <br>
     * -The file help.txt in the JAR <br>
     * -The HelpFormatter <br>
     * -Options.
     */
    private static void showHelp(HelpFormatter hf,
            Options options) {
        try {
            //First, print the header of help and brief summary of the behaviour
            InputStream f = RankerOCR.class.getResourceAsStream(
                    "/resources/help.txt");
            hf.printWrapped(new PrintWriter(System.out, true), 50,
                    getDocumentText(f));
            System.out.println("\n");
            //Second, print the options list
            hf.printHelp("java -jar Ranker-OCR [options]", options);
            try {
                //Finnaly print the rankers list
                System.out.println("\nRankers list:");
                List<Class> lst = PackageClassList.getClasses("ranking");
                lst.remove(ranking.Ranker.class);
                lst.stream().forEach((c) -> {
                    if (!c.isInterface()) {
                        System.out.println(" -" + c.getSimpleName());
                    }
                });
            } catch (ClassNotFoundException | IOException ex) {
                System.err.println(ex);
                System.exit(-11);
            }
        } catch (IOException ex) {
            System.exit(-5);
        }
    }
}
