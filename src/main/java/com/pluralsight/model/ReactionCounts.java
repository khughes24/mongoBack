package com.pluralsight.model;

public class ReactionCounts {

    public Integer Angry; //1
    public Integer Awesome; //2
    public Integer Boring; //3
    public Integer Care; //4
    public Integer Crazy; //5
    public Integer FakeNews; //6
    public Integer Haha; //7
    public Integer Lame; //8
    public Integer Legal; //9
    public Integer Like; //10
    public Integer Love; //11
    public Integer Meal; //12
    public Integer Sad; //13
    public Integer Scary; //14
    public Integer Wow; //15



    public ReactionCounts updateReactionCounts(ReactionCounts reactionCounts, String reactionId){
        if(reactionId == "1"){
            reactionCounts.Angry ++;
        }
        if(reactionId == "2"){
            reactionCounts.Awesome ++;
        }
        if(reactionId == "3"){
            reactionCounts.Boring ++;
        }
        if(reactionId == "4"){
            reactionCounts.Care ++;
        }
        if(reactionId == "5"){
            reactionCounts.Crazy ++;
        }
        if(reactionId == "6"){
            reactionCounts.FakeNews ++;
        }
        if(reactionId == "7"){
            reactionCounts.Haha ++;
        }
        if(reactionId == "8"){
            reactionCounts.Lame ++;
        }
        if(reactionId == "9"){
            reactionCounts.Legal ++;
        }
        if(reactionId == "10"){
            reactionCounts.Like ++;
        }
        if(reactionId == "11"){
            reactionCounts.Love ++;
        }
        if(reactionId == "12"){
            reactionCounts.Meal ++;
        }
        if(reactionId == "13"){
            reactionCounts.Sad ++;
        }
        if(reactionId == "14"){
            reactionCounts.Scary ++;
        }
        if(reactionId == "15"){
            reactionCounts.Wow ++;
        }
        return reactionCounts;
    }

}

