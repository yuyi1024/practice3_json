import java.util.ArrayList;

public class Time {
    private String startTime;
    private String endTime;
    private Parameter parameter;

    public Time(String startTime, String endTime, Parameter parameter) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.parameter = parameter;
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

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
}
