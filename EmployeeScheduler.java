import java.util.*;

public class EmployeeScheduler {
    static final String[] DAYS={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    static final String[] SHIFTS={"Morning","Afternoon","Evening"};
    static final int MIN_EMPLOYEES=2, MAX_DAYS=5;
    static final List<String> EMPLOYEES=Arrays.asList("Ava","Ben","Carlos","Deepa","Ethan","Fatima","Grace","Henry","Isabella","Jason","Kiara","Liam");
    static final Map<String,Map<String,String>> preferences=new LinkedHashMap<>();
    static final Map<String,Map<String,List<String>>> schedule=new LinkedHashMap<>();
    static final Map<String,Integer> daysWorked=new LinkedHashMap<>();
    static final Random random=new Random(7);

    public static void main(String[] args){
        initialize(); collectPreferences(); preferredPass(); fillShortages(); printSchedule(); validate();
    }
    static void initialize(){
        for(String e:EMPLOYEES) daysWorked.put(e,0);
        for(String d:DAYS){ Map<String,List<String>> m=new LinkedHashMap<>(); for(String s:SHIFTS)m.put(s,new ArrayList<>()); schedule.put(d,m); }
    }
    static void collectPreferences(){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter M for manual preferences or press Enter for sample preferences: ");
        String mode=sc.nextLine().trim().toLowerCase();
        for(int i=0;i<EMPLOYEES.size();i++){
            String e=EMPLOYEES.get(i); Map<String,String> p=new LinkedHashMap<>();
            for(int j=0;j<DAYS.length;j++){
                if(mode.equals("m")){
                    while(true){
                        System.out.print(e+" - "+DAYS[j]+" (morning/afternoon/evening): ");
                        String v=sc.nextLine().trim().toLowerCase();
                        String f=v.isEmpty()?v:v.substring(0,1).toUpperCase()+v.substring(1);
                        if(Arrays.asList(SHIFTS).contains(f)){p.put(DAYS[j],f);break;}
                        System.out.println("Invalid shift. Please try again.");
                    }
                } else p.put(DAYS[j],SHIFTS[(i+j)%3]);
            }
            preferences.put(e,p);
        }
    }
    static boolean working(String e,String d){ for(String s:SHIFTS) if(schedule.get(d).get(s).contains(e)) return true; return false; }
    static boolean canAssign(String e,String d){ return daysWorked.get(e)<MAX_DAYS && !working(e,d); }
    static boolean assign(String e,String d,String s){ if(!canAssign(e,d))return false; schedule.get(d).get(s).add(e); daysWorked.put(e,daysWorked.get(e)+1); return true; }
    static void preferredPass(){
        for(String d:DAYS) for(String s:SHIFTS){
            List<String> interested=new ArrayList<>();
            for(String e:EMPLOYEES) if(preferences.get(e).get(d).equals(s)&&canAssign(e,d)) interested.add(e);
            Collections.shuffle(interested,random);
            for(String e:interested){ if(schedule.get(d).get(s).size()>=MIN_EMPLOYEES)break; assign(e,d,s); }
        }
    }
    static void fillShortages(){
        for(String d:DAYS) for(String s:SHIFTS) while(schedule.get(d).get(s).size()<MIN_EMPLOYEES){
            List<String> c=new ArrayList<>(); for(String e:EMPLOYEES)if(canAssign(e,d))c.add(e); if(c.isEmpty())break;
            Collections.shuffle(c,random); c.sort(Comparator.comparingInt(daysWorked::get)); if(!assign(c.get(0),d,s))break;
        }
    }
    static void printSchedule(){
        System.out.println("\nFINAL WEEKLY SCHEDULE\n==========================================================");
        for(String d:DAYS){ System.out.println("\n"+d+"\n----------------------------------------------------------"); for(String s:SHIFTS)System.out.printf("%-12s: %s%n",s,schedule.get(d).get(s)); }
        System.out.println("\nEMPLOYEE WORKLOAD\n----------------------------------------------------------"); for(String e:EMPLOYEES)System.out.printf("%-12s: %d day(s)%n",e,daysWorked.get(e));
    }
    static void validate(){
        boolean ok=true; System.out.println("\nVALIDATION\n----------------------------------------------------------");
        for(String d:DAYS)for(String s:SHIFTS)if(schedule.get(d).get(s).size()<MIN_EMPLOYEES){System.out.println("Shortage: "+d+" "+s);ok=false;}
        for(String e:EMPLOYEES){ if(daysWorked.get(e)>MAX_DAYS){System.out.println("Too many days: "+e);ok=false;} for(String d:DAYS){int n=0;for(String s:SHIFTS)if(schedule.get(d).get(s).contains(e))n++;if(n>1){System.out.println("Multiple shifts: "+e+" "+d);ok=false;}} }
        if(ok)System.out.println("Schedule passed all validation checks.");
    }
}
