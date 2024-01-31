package com.warning.utils.normalize;

public class SummaryBody {
    private String summary;
    private Integer start;
    private Integer end;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public SummaryBody(String summary, Integer start, Integer end) {
        this.summary = summary;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "SummaryBody{" +
                "summary='" + summary + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}