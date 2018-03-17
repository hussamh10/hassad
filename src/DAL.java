import competition.Group;
import competition.Match;
import competition.Player;
import competition.Team;
import competition.results.GroupResult;
import competition.results.MatchResult;
import competition.results.Result;
import competition.results.TournamentResult;
import player.Info;
import player.User;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DAL {

    private ArrayList<Group> groups;
    private ArrayList<Match> matches;
    private ArrayList<Team> teams;
    private ArrayList<Player> players;

    private ArrayList<User> users;
    private ArrayList<Info> infoArrayList;


    private ArrayList<GroupResult> groupResults;
    private ArrayList<MatchResult> matchResults;
    private ArrayList<TournamentResult> tournamentResults;


    public DAL(){

        initArrayLists();

        readTeams();
        readPlayers();
        readGroups();
        readMatches();

        readInfo();
        readUsers();

        readGroupResults();
        readMatchResults();
        readTournamentResults();
//        readPredictions();

    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Info> getInfoArrayList() {
        return infoArrayList;
    }

    public ArrayList<GroupResult> getGroupResults() {
        return groupResults;
    }

    public ArrayList<MatchResult> getMatchResults() {
        return matchResults;
    }

    public ArrayList<TournamentResult> getTournamentResults() {
        return tournamentResults;
    }

    private void readTeams(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/Teams.txt")));

            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.teams.add(new Team(
                        Integer.parseInt(obj[0]),
                        obj[1],
                        obj[2]
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Teams.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading Teams.txt");
            e.printStackTrace();
        }
    }

    private void readPlayers(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/Players.txt")));

            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.players.add(new Player(
                        Integer.parseInt(obj[0]),
                        obj[1],
                        obj[2],
                        getTeamByID(Integer.parseInt(obj[3])),
                        obj[4]
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Players.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading Players.txt");
            e.printStackTrace();
        }
    }

    private Team getTeamByID(int i) {
        for(Team team: this.teams){
            if(team.getId() == i)
                return team;
        }

        return null;
    }

    private void readTournamentResults() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/TournamentResults.txt")));

            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.tournamentResults.add(new TournamentResult(
                        getPlayerByID(Integer.parseInt(obj[0])),
                        getPlayerByID(Integer.parseInt(obj[1])),
                        getPlayerByID(Integer.parseInt(obj[2])),
                        getTeamByID(Integer.parseInt(obj[3])),
                        getTeamByID(Integer.parseInt(obj[4])),
                        getTeamByID(Integer.parseInt(obj[5])),
                        getTeamByID(Integer.parseInt(obj[6]))
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error TournamentResults.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading TournamentResults.txt");
            e.printStackTrace();
        }
    }

    private Player getPlayerByID(int i) {
        for(Player player : this.players){
            if(player.getId() == i)
                return player;
        }
        return null;
    }

    private void readMatchResults() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/MatchResults.txt")));

            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.matchResults.add(new MatchResult(
                        Integer.parseInt(obj[0]),
                        Integer.parseInt(obj[1]),
                        getTeamByID(Integer.parseInt(obj[1])),
                        getMatchByID(Integer.parseInt(obj[2]))
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error MatchResults.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading MatchResults.txt");
            e.printStackTrace();
        }
    }

    private Match getMatchByID(int i) {
        for(Match match : this.matches){
            if(match.getId() == i)
                return match;
        }
        return null;
    }

    private void readGroupResults() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/GroupResults.txt")));

            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.groupResults.add(new GroupResult(
                        getTeamByID(Integer.parseInt(obj[0])),
                        getTeamByID(Integer.parseInt(obj[1])),
                        getGroupByID(Integer.parseInt(obj[2]))
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error GroupResults.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading GroupResults.txt");
            e.printStackTrace();
        }
    }

    private Group getGroupByID(int i) {
        for(Group group : this.groups){
            if(group.getId() == i)
                return group;
        }
        return null;
    }

    private void readInfo() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/Info.txt")));

            String line;
            DateFormat df = new SimpleDateFormat("yyyy/mm/dd");


            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");


                this.infoArrayList.add(new Info(
                        Integer.parseInt(obj[0]),
                        obj[1],
                        obj[2],
//                    new Date(java.lang.String.valueOf(df.parse(obj[3]))),
                        null,
                        obj[4],
                        0
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Info.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading Info.txt");
            e.printStackTrace();
        }
//        catch (ParseException e) {
//            System.out.println("Error in reading Date from Info.txt");
//            e.printStackTrace();
//        }
    }

    private void readUsers() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/Users.txt")));

            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.users.add(new User(
                        UUID.fromString(obj[0]),
                        Integer.parseInt(obj[1]),
                        Integer.parseInt(obj[2]),
                        getInfoByID(Integer.parseInt(obj[0]))
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Users.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading Users.txt");
            e.printStackTrace();
        }
    }

    private Info getInfoByID(int i) {
        for(Info info : this.infoArrayList){
            if(info.getUserID() == i)
                return info;
        }
        return null;
    }

    private void readMatches() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/Matches.txt")));

            String line;
            DateFormat df = new SimpleDateFormat("yyyy/mm/dd");

            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                this.matches.add(new Match(
                        Integer.parseInt(obj[0]),
                        getTeamByID(Integer.parseInt(obj[1])),
                        getTeamByID(Integer.parseInt(obj[2])),
                        obj[3],
                        //new Date(String.valueOf(df.parse(obj[4]))),
                        null,
                        null,
                        Boolean.parseBoolean(obj[5]),
                        Integer.parseInt(obj[6])
                ));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Matches.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading Matches.txt");
            e.printStackTrace();
        }
//        catch (ParseException e) {
//            System.out.println("Error in reading Date from Matches.txt");
//            e.printStackTrace();
//        }
    }

    private void readGroups() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("db/Groups.txt")));

            String line;
            int groupID = 1;
            ArrayList<Team> teams = new ArrayList<>(); // sorted with points
            ArrayList<Integer> pts = new ArrayList<>(); // sorted with points
            ArrayList<Integer> pld = new ArrayList<>(); // sorted with points


            while((line = bufferedReader.readLine()) != null){
                String[] obj = line.split(",");

                int id = Integer.parseInt(obj[0]);

                if(id != groupID) {       // same as old

                    this.groups.add(new Group(groupID, teams, pts, pld,false,null));

                    teams = new ArrayList<>(); // sorted with points
                    pts = new ArrayList<>(); // sorted with points
                    pld = new ArrayList<>(); // sorted with points
                    groupID++;
                }
                teams.add(getTeamByID(Integer.parseInt(obj[1])));
                pts.add(Integer.parseInt(obj[2]));
                pld.add(Integer.parseInt(obj[3]));

            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Groups.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading Groups.txt");
            e.printStackTrace();
        }
    }

    private void getResultByID(int i) {
    }

    private User getUserByID(int i) {
        for(User user : this.users){
            if(user.getId() == UUID.fromString(String.valueOf(i)))
                return user;
        }
        return null;
    }

    private void initArrayLists() {

        groups = new ArrayList<>();
        matches = new ArrayList<>();
        teams = new ArrayList<>();
        players = new ArrayList<>();

        users = new ArrayList<>();
        infoArrayList = new ArrayList<>();

        groupResults = new ArrayList<>();
        matchResults = new ArrayList<>();
        tournamentResults = new ArrayList<>();
    }
}
