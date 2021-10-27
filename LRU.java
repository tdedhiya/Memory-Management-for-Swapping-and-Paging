import java.util.*;
class LRU{
  public static ArrayList<Page> lru(ArrayList<Page> pl){
    Page pt = new Page();
    int lru = (int)pl.get(0).last_used; 
    for(Page p : pl){
      if(p.last_used < lru){
        pt = p;
        lru = (int)p.last_used;
      }
    }
    pt.pid = -1;
    pt.page_no = -1;
    pl.set(pt.index,pt);
    return pl;
  }
}