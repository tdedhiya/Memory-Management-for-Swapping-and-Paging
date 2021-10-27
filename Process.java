class Process{
  public int pid;
  public int page_count;
  public int arrival_time;
  public int duration;
  public int curr_page;

  public Process(int pid, int page_count,int arrival_time,int duration,int curr_page){
    this.pid = pid;
    this.page_count = page_count;
    this.duration = duration;
    this.arrival_time = arrival_time;
    this.curr_page = curr_page;
  }
}