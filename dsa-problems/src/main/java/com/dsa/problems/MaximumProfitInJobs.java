package com.dsa.problems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MaximumProfitInJobs {

  public static int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    List<MeetingDetail> meetingDetails = new ArrayList<>();
    for(int i=0 ; i< startTime.length; i++ ){
      meetingDetails.add(new MeetingDetail(startTime[i], endTime[i],
          profit[i]));
    }
    meetingDetails.sort(Comparator.comparingInt(meetingDetail -> meetingDetail.endTime));
    return getMaximumProfit(meetingDetails);
  }

  public static int getMaximumProfit(List<MeetingDetail> meetingDetails){
    int size = meetingDetails.size();
    int[] profit = new int[size];
    profit[0] = meetingDetails.get(0).profit;
    for(int i =1; i < size ;i++){
      int includedProfit = meetingDetails.get(i).profit;
      int latestNonConflictingIndex = getLatestNonConflictingIndex(meetingDetails, i-1);
      if(latestNonConflictingIndex != -1){
        includedProfit = includedProfit + profit[latestNonConflictingIndex];
      }
      profit[i] = Math.max(includedProfit, profit[i-1]);
    }
    return profit[size-1];
  }

  public static int getLatestNonConflictingIndex(List<MeetingDetail> meetingDetails, int size){
    int startIndex = 0;
    int endIndex = size;
    while (startIndex <= endIndex){
      int midIndex = (startIndex + endIndex) / 2;
      if(meetingDetails.get(midIndex).endTime <= meetingDetails.get(size+1).startTime){
        if(meetingDetails.get(midIndex +1).endTime <= meetingDetails.get(size+1).startTime){
          startIndex =  midIndex + 1;
        } else {
          return midIndex;
        }
      } else {
        endIndex = midIndex -1;
      }
    }
    return -1 ;
  }

  public static void main(String[] args){

    int[] startTime = new int[] {1,2,3,4,6};
    int[] endTime = new int[] {3,5,10,6,9};
    int[] profit = new int[] {20,20,100,70,60};
    System.out.println("Maximum profit:" + jobScheduling(startTime, endTime, profit));
  }

  public static class MeetingDetail {
    int startTime;
    int endTime;
    int profit;
    public MeetingDetail(int startTime, int endTime, int profit){
      this.startTime = startTime;
      this.endTime = endTime;
      this.profit = profit;
    }
  }

}
