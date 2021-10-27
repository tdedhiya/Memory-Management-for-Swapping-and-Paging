import java.util.*;
import java.util.concurrent.TimeUnit;
class Main {

 public static int NUMBER_OF_PROCS = 150;
 public static int SIMULATION_DURATION = 60;
 public static int MAX_PROC_DURATION = 7;
 public static int PAGE_LIST_SIZE = 100;
 public static void main(String[] args) {
  for (int c = 0; c < 5; c++) {
 
  int sim_clk = 0;
  int page_count_options[] = new int[4];
  page_count_options[0] = 5;
  page_count_options[1] = 11;
  page_count_options[2] = 17;
  page_count_options[3] = 31;
  int swappedinProc = 0;
  Random rand = new Random();
  Page tp = new Page();

  for (int i = 0; i < 5; i++) {
   System.out.println("===================RUN============================");
   Page obj = new Page();
   ArrayList < Page > pl = obj.init_page_list();
   Process Q[] = new Process[NUMBER_OF_PROCS];
   for (int j = 0; j < NUMBER_OF_PROCS; j++) {
    rand.setSeed(j * 10000000);
    // all processes begin with page 0
    Q[j] = new Process(j, page_count_options[rand.nextInt(4)], rand.nextInt(60), rand.nextInt(MAX_PROC_DURATION), 0);
   }
   Arrays.sort(Q, new Comparator < Process > () {
    @Override
    public int compare(Process p1, Process p2) {
     return p1.arrival_time - p2.arrival_time;
    }
   });

   int ix = 0; // index to the start of process queue
   // at the beginning of every second, look for new processes
   for (sim_clk = 0; sim_clk < SIMULATION_DURATION; sim_clk++) {
    while (ix < NUMBER_OF_PROCS && Q[ix].arrival_time <= sim_clk) {
     // see if we have atleast 4 free pages
     if (obj.test_free_pages(pl, 4)) {
      Page p = obj.get_free_page(pl); // if yes, bring process in memory
      p.pid = Q[ix].pid;
      p.page_no = Q[ix].curr_page;
      p.brought_in_time = 1.0 * sim_clk;
      p.count = 1;
      p.last_used = sim_clk;
      pl = obj.updatePageList(p, pl);
      System.out.println("Page " + Q[ix].curr_page + " for process " + Q[ix].pid + " brought in at " + p.brought_in_time);
      swappedinProc++;
      ix++;
     } else
      break; // not enough memory
    }
    // Every 100ms a new request for a page is being made by all processes in memory
    for (int k = 0; k < 10; k++) {
     for (int j = 0; j < ix; j++) {
      if (Q[j].duration > 0) {
       Q[j].curr_page = obj.get_next_page_no(Q[j].curr_page, Q[j].page_count); // update current page no
       if (obj.page_exists_in_memory(pl, Q[j].pid, Q[j].curr_page)) { // page already exists in memory
        tp = obj.get_page_from_pid(pl, Q[j].pid, Q[j].curr_page);
        // Note that eviction algorithms like LRU, LFU might need to update some metadata at this point. For example, for LRU
        // we could have another variable 'last_used_time' in the page struct which would be updated to (sim_clk+0.1*i) which is
        // the time at which this page was referenced again. Since I am implementing FCFS, I don't need any extra book-keeping.
        if (tp == null) {
         System.out.println("potential bug, got NULL from on a page that exists");
         return;
        }
        tp.count++;
        tp.last_used = sim_clk;
        pl = obj.updatePageList(tp, pl);
        continue;
       }
       // if we are here then that means we refered a page which is not there in memory. So we need to bring it in.
       Page pg = obj.get_free_page(pl);
       if (pg == null) { // no free pages in memory!! need to evict a page
        System.out.println("Memory full, Page list:");
        obj.display_page_list(pl);
        switch (c) {
         case 0:
          pl = RandomC.rando(pl);
          break;
         case 1:
          pl = FCFS.fcfs(pl);
          break;
         case 2:
          pl = LRU.lru(pl);
          break;
         case 3:
          pl = LFU.lfu(pl);
          break;
         case 4:
          pl = MFU.mfu(pl);
          break;
        }
        obj.display_page_list(pl);
        pg = obj.get_free_page(pl);
       }
       pg.pid = Q[j].pid;
       pg.page_no = Q[j].curr_page;
       pg.brought_in_time = sim_clk + (0.1 * i);
       pg.last_used = sim_clk + (0.1 * i);
       pg.count = 0;
       pl = obj.updatePageList(pg, pl);
       System.out.println("Page " + Q[j].curr_page + " for process " + Q[j].pid + " brought in at " + pg.brought_in_time);
       swappedinProc++;
      }
     }
    }
    for (int a = 0; a < ix; a++) {
     if (Q[a].duration > 0) {
      Q[a].duration--;
      if (Q[a].duration == 0) {
       //process has finished execution . free pages in memory;
       System.out.println("Process " + Q[a].pid + " done. Freeing memory ... \n");
       obj.free_memory(pl, Q[a].pid);
      }
     }
    }
    try {
     //TimeUnit.SECONDS.sleep(1);
    } catch (Exception e) {}
   }
  }
  System.out.println("Averge number of processes that were successfully swapped in " + (swappedinProc / 5));
  System.out.println("............................................................................................");
 }
 }
}