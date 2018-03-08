package competition.results;

import competition.Group;
import competition.Team;

public class GroupResult extends Result{
    private Team qualifying_1;
    private Team qualifying_2;
    private Group group;

    public GroupResult(Team qualifying_1, Team qualifying_2, Group group) {
        this.qualifying_1 = qualifying_1;
        this.qualifying_2 = qualifying_2;
        this.group = group;
    }

    // Auto Generated Code

    public Team getQualifying_1() {
        return qualifying_1;
    }

    public void setQualifying_1(Team qualifying_1) {
        this.qualifying_1 = qualifying_1;
    }

    public Team getQualifying_2() {
        return qualifying_2;
    }

    public void setQualifying_2(Team qualifying_2) {
        this.qualifying_2 = qualifying_2;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "GroupResult{" +
                "qualifying_1=" + qualifying_1 +
                ", qualifying_2=" + qualifying_2 +
                ", group=" + group +
                '}';
    }
}
