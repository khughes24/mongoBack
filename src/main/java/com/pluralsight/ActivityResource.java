package com.pluralsight;

import com.pluralsight.model.Activity;
import com.pluralsight.model.User;
import com.pluralsight.repository.ActivityRepository;
import com.pluralsight.repository.ActivityRepositoryStub;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("activities")
public class ActivityResource {
    private ActivityRepository activityRepository = new ActivityRepositoryStub();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Activity> getAllActivities(){
        List<Activity> gotActivities = new ArrayList<Activity>();
        gotActivities = activityRepository.findAllActivities();
        return gotActivities;
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("{activityId}")
    public Response getActivity(@PathParam ("activityId") String activityId){
        if(activityId == null || activityId.length() < 3){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Activity gotActivity = activityRepository.findActivity(activityId);
        if(gotActivity == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(gotActivity).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("{activityId}/user")
    public User getActivityUser(@PathParam ("activityId") String activityId){
        System.out.println("output test");
        return activityRepository.findActivity(activityId).getUser();
    }

    @POST
    @Path("activity")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Activity createActivityParams(MultivaluedMap<String,String> formParams){
        System.out.println(formParams.getFirst("description"));
        System.out.println(formParams.getFirst("duration"));
        Activity activity = new Activity();
        activity.setDescription(formParams.getFirst("description"));
        activity.setDuration(Integer.parseInt(formParams.getFirst("duration")));
        activityRepository.create(activity);
        return activity;
    }
    @PUT
    @Path("{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateActivity(Activity activity){
        System.out.println(activity.getId());
        activity = activityRepository.update(activity);
        return Response.ok().entity(activity).build();
    }

    @DELETE
    @Path("{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response delete(@PathParam("activityId") String activityId){
        System.out.println(activityId);
        activityRepository.delete(activityId);
        return Response.ok().build();
    }


    @POST
    @Path("activity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Activity createActivity(Activity activity){
        System.out.println(activity.getDescription());
        System.out.println(activity.getDuration());
        activityRepository.create(activity);
        return activity;
    }


}
