import java.util.*;

class MFU{
  public static ArrayList<Page> mfu(ArrayList<Page> pl){
    Page pt = new Page();
    int max = pl.get(0).count; 
    for(Page p : pl){
      if(p.count > max){
        pt = p;
        max = p.count;
      }
    }
    pt.pid = -1;
    pt.page_no = -1;
    pl.set(pt.index,pt);
    return pl;
  }
}