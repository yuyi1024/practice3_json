public class WetherRow {
    private String startTime;
    private String endTime;
    private String wx;
    private String pop;
    private String minT;
    private String maxT;

    public WetherRow() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getMinT() {
        return minT;
    }

    public void setMinT(String minT) {
        this.minT = minT;
    }

    public String getMaxT() {
        return maxT;
    }

    public void setMaxT(String maxT) {
        this.maxT = maxT;
    }

    public boolean timeEquals(String startTime, String endTime){
        if(this.startTime.equals(startTime) && this.endTime.equals(endTime)){
            return true;
        } else{
            return false;
        }

    }
}
