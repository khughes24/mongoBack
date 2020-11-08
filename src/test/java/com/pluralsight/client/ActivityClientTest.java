package com.pluralsight.client;

    import com.pluralsight.model.Activity;
    import com.pluralsight.model.ActivitySearch;
    import org.junit.Test;

    import java.util.ArrayList;
    import java.util.List;

    import static org.junit.Assert.*;

    public class ActivityClientTest {

        /*
        @Test
        public void testCreate(){
            ActivityClient client = new ActivityClient();
            Activity activity = new Activity();
            activity.setDescription("Swimming");
            activity.setDuration(90);
            activity = client.create(activity);
            assertNotNull(activity);
        }

        @Test
        public void testGet(){
            ActivityClient client = new ActivityClient();
            Activity activity = client.get("333");
            System.out.println(activity);

            assertNotNull(activity);
        }

        @Test
        public void testGetList(){
            ActivityClient client = new ActivityClient();
            List<Activity> activities = client.get();
            System.out.println(activities);

            assertNotNull(activities);
        }
*/
        @Test
        public void testPut(){
            Activity activity = new Activity();
            activity.setId("3456");
            activity.setDescription("Motocross");
            activity.setDuration(120);
            ActivityClient client = new ActivityClient();
            client.update(activity);
            assertNotNull(activity);
        }


        @Test
        public void testSearchObject(){

            ActivitySearchClient client = new ActivitySearchClient();

            List<String> searchValues = new ArrayList<String>();
            searchValues.add("biking");
            searchValues.add("skiing");

            ActivitySearch search = new ActivitySearch();
            search.setDescriptions(searchValues);
            search.setDurationFrom(30);
            search.setDurationTo(55);

            List<Activity> activities = client.search(search);
            System.out.println(activities);
            assertNotNull(activities);
        }


        @Test
        public void testSearch(){
            ActivitySearchClient client = new ActivitySearchClient();
            String param = "description";
            List<String> searchValues = new ArrayList<String>();
            searchValues.add("Tennis");
            searchValues.add("Swimming");

            String secondParam = "durationFrom";
            int durationFrom = 30;

            String thirdParam = "durationTo";
            int durationTo = 55;

            List<Activity> activities = client.search(param, searchValues, secondParam, durationFrom, thirdParam, durationTo);
            System.out.println(activities);

            assertNotNull(activities);
        }


        @Test
        public void testDelete(){
            ActivityClient client = new ActivityClient();
            client.delete("1234");
        }

        @Test(expected = RuntimeException.class)
        public void testGetWithBadRequest(){
            ActivityClient client = new ActivityClient();
            client.get("31");
        }
}
