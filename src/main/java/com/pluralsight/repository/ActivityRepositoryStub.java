package com.pluralsight.repository;

import java.util.ArrayList;
import java.util.List;
import com.pluralsight.model.Activity;
import com.pluralsight.model.ActivitySearch;
import com.pluralsight.model.User;

public class ActivityRepositoryStub implements ActivityRepository {

    @Override
    public List<Activity> findAllActivities(){
        List<Activity> activities = new ArrayList<Activity>();
        Activity activity1 = new Activity();
        activity1.setDescription("Swimming");
        activity1.setDuration(55);
        activities.add(activity1);

        Activity activity2 = new Activity();
        activity2.setDescription("Cycling");
        activity2.setDuration(120);
        activities.add(activity2);
        return activities;

    }

    @Override
    public Activity findActivity(String activityId) {
        Activity activity1 = new Activity();
        activity1.setId("333");
        activity1.setDescription("Swimming");
        activity1.setDuration(55);
        User user = new User();
        user.setId("24");
        user.setName("Laura");
        activity1.setUser(user);
        return activity1;
    }

    @Override
    public void create(Activity activity) {
        //insert into db
    }

    @Override
    public Activity update(Activity activity){
        return activity;
    }

    @Override
    public void delete(String activityId) {

    }

    @Override
    public List<Activity> findByDescription(List<String> descriptions, int durationFrom, int durationTo) {
         List<Activity> activities = new ArrayList<Activity>();
         Activity activity = new Activity();
         activity.setId("7896");
         activity.setDescription("Tennis");
         activity.setDuration(55);
         activities.add(activity);
         return activities;
    }

    @Override
    public List<Activity> findByConstraints(ActivitySearch search) {
        List<Activity> activities = new ArrayList<Activity>();
        Activity activity = new Activity();
        activity.setId("7896");
        activity.setDescription("Tennis");
        activity.setDuration(55);
        activities.add(activity);
        return activities;
    }


}
