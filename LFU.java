import java.util.*;

class LFU{
  public static ArrayList<Page> lfu(ArrayList<Page> pl){
    Page pt = new Page();
    int min = pl.get(0).count; 
    for(Page p : pl){
      if(p.count < min){
        pt = p;
        min = p.count;
      }
    }
    pt.pid = -1;
    pt.page_no = -1;
    pl.set(pt.index,pt);
    return pl;
  }
}