/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ritabookmark;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author voice
 */
public class RITABOOKMARK extends JFrame implements ActionListener{
    public static HashSet<String> set = new HashSet<>();
    public String currentCommand = "";
    @Override
    public void actionPerformed(ActionEvent e) {
        currentCommand = e.getActionCommand();
        if (currentCommand.equals("exit")) System.exit(0);
        else handleCommand(currentCommand);
        textInput.setText("");
        textOutputFrame.setPreferredSize(new Dimension(this.getWidth(), 20*(textOutputFrame.getText().length()-textOutputFrame.getText().replaceAll("\n", "").length())));
    }
    
    /*long prev;
    public void tickSys(){
        prev = System.currentTimeMillis();
        while (true){
            if (System.currentTimeMillis() - prev > 6000){
                handleCommand("check false");
                prev = System.currentTimeMillis();
            }
        }
    }*/
    
    public void handleCommand(String command){
        if (command.startsWith("check")){
                Iterator iter = set.iterator();
                String currentname;
                while (iter.hasNext()){
                    currentname = (String) iter.next();
                    try {
                        boolean b = checkwriteYTSite(currentname);
                        textOutputFrame.setText(textOutputFrame.getText() + "\n" + currentname + " has changed: " + b);
                    } catch (IOException ex) {
                        
                    }
                }
                command = "";
                textOutputFrame.setText(textOutputFrame.getText() + "\n");
        }
        else if (command.startsWith("delete")){
            try {
                File f1 = new File(files.getCanonicalPath() + "\\" + command.split(" ")[1].replace("/", "_"));
                f1.delete();
                File f2 = new File(logs.getCanonicalPath() + "\\" + command.split(" ")[1].replace("/", "_"));
                f2.delete();
                set.remove(f1.getCanonicalPath().substring(files.getCanonicalPath().length()+1).replace("_", "/"));
                textOutputFrame.setText(textOutputFrame.getText() + "\nSuccesfully Deleted");
            } catch (IOException ex) {
                textOutputFrame.setText(textOutputFrame.getText() + "\n" + ex.getMessage());
            }
        }
        else {
                if (!command.contains(" ") && (command.startsWith("/user/") || command.startsWith("/channel/"))){
                    try {
                        checkwriteYTSite(command);
                        updateSet();
                    } catch (IOException ex) {
                        textOutputFrame.setText(textOutputFrame.getText() + "\n" + ex.getMessage());
                    }
                } else{
                    textOutputFrame.setText(textOutputFrame.getText() + "\n" + "Incorrect format: Please enter: \n\t/channel/[code]\t* \n\t/user/[name]\t* \n\tdelete [*] \n\tcheck \n\texit");
                    return;
                }
        }
    }
    
    
    public RITABOOKMARK(){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                setTitle("Enter '/channel/[code]' or '/user/[name]' or 'check'");
                setSize(500, 300);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }
    
    public void updateSet() throws IOException{
        set.clear();
        if (files.isDirectory()){
            File[] allfiles = files.listFiles();
            if (allfiles.length > 0)
                for (File f : allfiles){
                    set.add(f.getCanonicalPath().substring(files.getCanonicalPath().length()+1).replace("_", "/"));
                }
        }
    }
    
    /**
     * @param args the command line arguments
     */  
    public static File files;
    public static File logs;
    public static JTextArea textOutputFrame;
    public static JTextField textInput;
    public static void main(String[] args) throws IOException {
        RITABOOKMARK rt = new RITABOOKMARK();
        rt.setSize(500, 400);
        rt.setResizable(false);
        
        
        File jarLoc = new File("dist/RITABOOKMARK.jar");
        System.out.println(jarLoc.getAbsolutePath());
        System.out.println(Paths.get("").toAbsolutePath().toString());
        try {
            files   = new File(Paths.get("").toAbsolutePath().toString() + "/src/files");
            logs    = new File(Paths.get("").toAbsolutePath().toString() + "/src/logs");
        } catch (Exception ex){
            
        }
        if (!files.exists()){
            files   = new File(Paths.get("").toAbsolutePath().toString() + "/files");
            logs    = new File(Paths.get("").toAbsolutePath().toString() + "/logs");
        }
        
        
        if (files.isDirectory()){
            File[] allfiles = files.listFiles();
            if (allfiles.length > 0)
                for (File f : allfiles){
                    set.add(f.getCanonicalPath().substring(files.getCanonicalPath().length()+1).replace("_", "/"));
                }
        }
        textInput = new JTextField(1);
        textInput.setBounds(0, 0, rt.getWidth(), 20);
        textInput.setPreferredSize(new Dimension(rt.getWidth(), 20));
        textInput.setEditable(true);
        textInput.addActionListener(rt);
        textInput.setEnabled(true);
        textInput.setVisible(true);
       
        
        
        textOutputFrame = new JTextArea("3");
        textOutputFrame.setBounds(0, 20, rt.getWidth(), (rt.getHeight()-20));
        textOutputFrame.setLocation(0, 20);
        textOutputFrame.setPreferredSize(new Dimension(rt.getWidth(), rt.getHeight()-20));
        textOutputFrame.setEnabled(true);
        
        textOutputFrame.setLineWrap(true);
        textOutputFrame.setEditable(false);
        textOutputFrame.setVisible(true);
        
        JScrollPane textOutput = new JScrollPane(textOutputFrame);
        textOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        textOutputFrame.setText("");
        textOutputFrame.setText(textOutputFrame.getText()+ "\n" +Paths.get("").toAbsolutePath().toString());
        textOutputFrame.setText(textOutputFrame.getText()+ "\n" +files.getAbsolutePath());
        
        rt.add(textInput);
        //rt.add(textOutputFrame);
        rt.add(textOutput);
        //rt.pack();
        
        rt.setVisible(true);
        
    }
    /**
     * 
     * @param channelName
     * @return  if the data pulled from the site has changed since last call
     * @throws IOException 
     */
    public static boolean checkwriteYTSite(String channelName) throws IOException{
        
        File f = new File(files.getAbsolutePath() + "\\" + channelName.replaceAll("/", "_"));
        File f2 = new File(logs.getAbsolutePath() + "\\" + channelName.replaceAll("/", "_"));
        if (f.createNewFile()){
            PrintWriter pw = new PrintWriter(f.getCanonicalPath());
            PrintWriter pw2 = new PrintWriter(f2.getCanonicalPath());
            pw.write(readYTSite(channelName).replaceAll("[^A-Za-z]+", ""));
            pw2.write(readYTSite(channelName).replaceAll("[^A-Za-z]+", ""));
            pw.flush();
            pw2.flush();
            pw.close();
            pw = null;
            pw2.close();
            pw2 = null;
            return false;
        } else {
            BufferedReader inputHandler = new BufferedReader(new FileReader(f));
            String current = inputHandler.readLine();
            if (current.equals(readYTSite(channelName.replaceAll("_", "\\")).replaceAll("[^A-Za-z]+", ""))){
                inputHandler.close();
                inputHandler = null;
                return false;
            } else {
                //Get all text from f2
                BufferedReader inputHandler2 = new BufferedReader(new FileReader(f2));
                String temp = "";
                current = inputHandler2.readLine();
                while (current != null){
                    temp = temp + current;
                    current = inputHandler2.readLine();
                }
                
                PrintWriter pw2 = new PrintWriter(f2.getCanonicalPath());
                f.delete();
                f.createNewFile();
                PrintWriter pw = new PrintWriter(f.getCanonicalPath());
                pw.write(readYTSite(channelName.replaceAll("_", "\\")).replaceAll("[^A-Za-z]+", ""));
                pw2.write(temp + "\n" + readYTSite(channelName.replaceAll("_", "\\")).replaceAll("[^A-Za-z]+", ""));
                pw.flush();
                pw2.flush();
                pw.close();
                pw = null;
                pw2.close();
                pw2 = null;
                inputHandler2.close();
                inputHandler2 = null;
                return true;
            }
        }
    }
    public static String readYTSite(String urlString) throws MalformedURLException, IOException{
        urlString = "https://www.youtube.com" + urlString;
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream input = con.getInputStream();
        BufferedReader inputHandler = new BufferedReader(new InputStreamReader(input));
        String current;
        current = inputHandler.readLine();
        boolean b = false;
        int sent = 0;
        String retVal = "";
        while (current != null){
            if (current.equals("      <span class=\"\" >Uploads</span>"))
                b = true;
            
            if (b){
                if (current.trim().startsWith("<h3 class=\"yt-lockup-title \"><a class=\"yt-uix-sessionlink yt-uix-tile-link  spf-link  yt-ui-ellipsis yt-ui-ellipsis-2\" dir=\"ltr\" title=")){
                    retVal += current.trim().substring(136).substring(0, current.trim().substring(136).indexOf("\""));
                }
                
                if (current.startsWith("      <a href=\"/playlist?"))
                    b = false;
            }
            

            current = inputHandler.readLine();
        }
        return retVal;
    }

    
    
}
