/**
 * author: Tanyunfeng Chen
 * 10/16/2021
 * Reading cookie data from csv file, and find the 
 * most active cookie by spcific date.
 */
import java.util.*;
import java.nio.file.*;
import java.io.*;

class most_active_cookie{
    private  final static int dateLength = 10;
    //read csv file
    public static List<String> readFile(String name){
        List<String> file = new ArrayList<>();
        try{
            file = new ArrayList<> (Files.readAllLines(Paths.get(name)));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return file;
    }
    //get Date from the each one line.
    public static String get_Date(List<String> file, int index){
            String check_date = file.get(index);
            String[] infor = check_date.split(",");
            check_date = infor[1].substring(0, dateLength);
            if(isValid(check_date)){
                return check_date;
            }
            else{
                return "Invalid";
            }
    }
    //check if date of each cookie is valid
    private static boolean isValid(String check_date){
        String[] date = check_date.split("-");
        if(date.length!=3){
            return false;
        }
        else{
            int year;
            int month;
            int day;
            // check format
            try {
                 year = Integer.parseInt(date[0]);
                 month = Integer.parseInt(date[1]);
                 day = Integer.parseInt(date[2]);
            } 
            catch(NumberFormatException e){
                return false;
            }
            if(year<0){
                return false;
            }
            if(month<0 || month>12){
                return false;
            }
            if(day<0 || day>31){
                return false;
            }
        }
        return true;
    }
    public static List<String> cookieBydate(String Date, List<String> file){
        int left = 0;
        int right = file.size();
        int index = Integer.MAX_VALUE;
        //binary search 
        while(left<right){
            int mid = left + (right-left)/2;
            String check_date = get_Date(file,mid);
            if(check_date.equals("Invalid")){
                left++;
                continue;
            }
            if(Date.compareTo(check_date)==0){
                index = mid;
                break;
            }
            else if(Date.compareTo(check_date)>0){
                right = mid;
            }
            else{
                left = mid+1;
            }
        }
        //check if found cookie by specific date
        if(index == Integer.MAX_VALUE){
            return new ArrayList<String>();
        }
        else{
            List<String> ans = new ArrayList<>();
            int to_left = index;
            int to_right = index-1;
            // get all cookie of that date
            while(to_right >= 0){
                if(Date.compareTo(get_Date(file, to_right))==0){
                    String cookie = file.get(to_right);
                    int endIndex = cookie.indexOf(",");
                    cookie = cookie.substring(0, endIndex);
                    ans.add(cookie);
                    to_right--;
                }
                else{
                    break;
                }
            }
            while(to_left < file.size()){
                if(Date.compareTo(get_Date(file, to_left))==0){
                    String cookie = file.get(to_left);
                    int endIndex = cookie.indexOf(",");
                    cookie = cookie.substring(0, endIndex);
                    ans.add(cookie);
                    to_left++;
                }
                else{
                    break;
                }
            }
            return ans;
        }
    }
    
        public static void main(String[] args){
            if(args.length != 3){
                System.out.println("Invalid Input format");
                return;
            }
            List<String> file = readFile(args[0]);
            if(args[1].equals("-d")){
                String Date = args[2];
                List<String> Cookies = cookieBydate(Date,file);
                Map<String,Integer> map = new HashMap<>();
                List<String> ans = new ArrayList<String>();
                //recode the number of each cookie
                for(int i = 0;i<Cookies.size();i++){
                    map.put(Cookies.get(i),map.getOrDefault(Cookies.get(i), 0)+1);
                }
                int max = -1;
                //compare amd find the most active   
                for(String key : map.keySet()){
                    if(max == map.get(key)){
                        ans.add(key);
                    }
                    else if(max<map.get(key)){
                        max = map.get(key);
                        ans = new ArrayList<>();
                        ans.add(key);
                    }
                    else{
                        continue;
                    }
                }
                //My csv file contains " " at beginning of each when I read it by using VS code.
                //To make sure the output format is good.
                for(String cookie : ans){
                    System.out.println(cookie.replace("\"", ""));
                }
                return;
            }
        }
}
