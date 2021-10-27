import java.util.*;

class RandomC{
  public static ArrayList<Page> rando(ArrayList<Page> pl){
    int count =0;
    Random rand = new Random();
    int r = rand.nextInt(Main.PAGE_LIST_SIZE);
    Page pt = new Page();
    for(Page p : pl){
      if(p.pid > 0 && count < r){
        pt = p;
      }
      count++;
    }
    pt.pid = -1;
    pt.page_no = -1;
    pl.set(pt.index,pt);
    return pl;
  }
}