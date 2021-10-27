import java.util.*;

class FCFS{
  public static ArrayList<Page> fcfs(ArrayList<Page> pl){
    Page pt = pl.get(0);
    for(Page p : pl){
      if(p.brought_in_time < pt.brought_in_time){
        pt = p;
      }
    }
    System.out.println("EVICTED: "+pt.pid+"  "+pt.count+"  "+pt.last_used);
    pt.pid = -1;
    pt.page_no = -1;
    pl.set(pt.index,pt);
    return pl;
  }
}