import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

abstract class User {
    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Abstract method for polymorphism
    public abstract void generateReport();
}

class Athlete extends User {
    private List<Achievement> achievements = new ArrayList<>();
    private List<Event> registeredEvents = new ArrayList<>();

    public Athlete(String id, String name) {
        super(id, name);
    }

    public void addAchievement(Achievement achievement) throws DuplicateAchievementException {
        for (Achievement ach : achievements) {
            if (ach.getTitle().equals(achievement.getTitle()) && ach.getEventName().equals(achievement.getEventName())) {
                throw new DuplicateAchievementException("Achievement already exists for this athlete.");
            }
        }
        achievements.add(achievement);
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void registerForEvent(Event event) {
        registeredEvents.add(event);
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    @Override
    public void generateReport() {
        System.out.println("Athlete Report for " + getName() + " (ID: " + getId() + "):");
        System.out.println("Registered Events:");
        for (Event ev : registeredEvents) {
            System.out.println("- " + ev.getName() + " on " + ev.getDate());
        }
        System.out.println("Achievements:");
        for (Achievement ach : achievements) {
            System.out.println("- " + ach.getTitle() + " at " + ach.getEventName() + " on " + ach.getDate() + " (Level: " + ach.getAwardLevel() + ")");
        }
    }
}

class Organizer extends User {
    public Organizer(String id, String name) {
        super(id, name);
    }

    @Override
    public void generateReport() {
        System.out.println("Organizer Report for " + getName() + " (ID: " + getId() + "):");
        System.out.println("This would typically include overall system reports, but not implemented in detail for Review 1.");
    }
}

class Event {
    private String name;
    private LocalDate date;
    private String location;
    private int capacity;
    private List<Athlete> participants = new ArrayList<>();

    public Event(String name, LocalDate date, String location, int capacity) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Athlete> getParticipants() {
        return participants;
    }

    public void addParticipant(Athlete athlete) throws EventFullException, DuplicateRegistrationException {
        if (participants.size() >= capacity) {
            throw new EventFullException("Event capacity exceeded.");
        }
        if (participants.contains(athlete)) {
            throw new DuplicateRegistrationException("Athlete already registered for this event.");
        }
        participants.add(athlete);
        athlete.registerForEvent(this);
    }
}

class Achievement {
    private String title;
    private String eventName;
    private LocalDate date;
    private String awardLevel;

    public Achievement(String title, String eventName, LocalDate date, String awardLevel) {
        this.title = title;
        this.eventName = eventName;
        this.date = date;
        this.awardLevel = awardLevel;
    }

    public String getTitle() {
        return title;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAwardLevel() {
        return awardLevel;
    }
}

class EventManager {
    private Map<Event, List<Athlete>> eventParticipants = new HashMap<>();
    private List<Event> events = new ArrayList<>();

    public void createEvent(String name, LocalDate date, String location, int capacity) throws InvalidDateException {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Event date cannot be in the past.");
        }
        Event event = new Event(name, date, location, capacity);
        events.add(event);
        eventParticipants.put(event, event.getParticipants());
    }

    public List<Event> getEvents() {
        return events;
    }

    public void registerAthleteForEvent(Athlete athlete, Event event) throws EventFullException, DuplicateRegistrationException {
        event.addParticipant(athlete);
    }

    public void searchParticipantsByEvent(Event event) {
        List<Athlete> participants = eventParticipants.getOrDefault(event, new ArrayList<>());
        System.out.println("Participants for event " + event.getName() + ":");
        for (Athlete ath : participants) {
            System.out.println("- " + ath.getName() + " (ID: " + ath.getId() + ")");
        }
    }

    public void sortParticipantsByEvent(Event event) {
        List<Athlete> participants = new ArrayList<>(eventParticipants.getOrDefault(event, new ArrayList<>()));
        participants.sort(Comparator.comparing(User::getName));
        System.out.println("Sorted Participants for event " + event.getName() + ":");
        for (Athlete ath : participants) {
            System.out.println("- " + ath.getName() + " (ID: " + ath.getId() + ")");
        }
    }
}

class AwardManager {
    public void addAchievementToAthlete(Athlete athlete, String title, String eventName, LocalDate date, String awardLevel) throws DuplicateAchievementException, InvalidDateException {
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Achievement date cannot be in the future.");
        }
        Achievement achievement = new Achievement(title, eventName, date, awardLevel);
        athlete.addAchievement(achievement);
    }

    public void generateAchievementReportByAthlete(Athlete athlete) {
        athlete.generateReport();
    }

    public void generateAchievementReportByAwardLevel(String awardLevel) {
        // For simplicity, assume we have access to all athletes; in full system, we'd have a list
        System.out.println("Achievements for award level " + awardLevel + ":");
        // This would iterate over all athletes, but since we don't have global list yet, placeholder
        System.out.println("Placeholder: List achievements matching level.");
    }
}

// Custom Exceptions
class EventFullException extends Exception {
    public EventFullException(String message) {
        super(message);
    }
}

class DuplicateAchievementException extends Exception {
    public DuplicateAchievementException(String message) {
        super(message);
    }
}

class InvalidDateException extends Exception {
    public InvalidDateException(String message) {
        super(message);
    }
}

class DuplicateRegistrationException extends Exception {
    public DuplicateRegistrationException(String message) {
        super(message);
    }
}

// Reminder Thread
class EventReminderThread extends Thread {
    private List<Event> events;

    public EventReminderThread(List<Event> events) {
        this.events = events;
    }

    @Override
    public void run() {
        while (true) {
            LocalDate today = LocalDate.now();
            for (Event ev : events) {
                if (ev.getDate().isEqual(today.plusDays(1))) { // Reminder for tomorrow
                    System.out.println("Reminder: Event " + ev.getName() + " is tomorrow at " + ev.getLocation() + "!");
                }
            }
            try {
                Thread.sleep(60000); // Check every minute
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class AthleteEventAchievementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static EventManager eventManager = new EventManager();
    private static AwardManager awardManager = new AwardManager();
    private static Map<String, Athlete> athletes = new HashMap<>();
    private static Map<String, Organizer> organizers = new HashMap<>();

    public static void main(String[] args) {
        // Start reminder thread
        EventReminderThread reminderThread = new EventReminderThread(eventManager.getEvents());
        reminderThread.start();

        while (true) {
            System.out.println("\nAthlete Event & Achievement Management System");
            System.out.println("1. Register Athlete");
            System.out.println("2. Register Organizer");
            System.out.println("3. Create Event (Organizer)");
            System.out.println("4. Register for Event (Athlete)");
            System.out.println("5. Add Achievement (Organizer)");
            System.out.println("6. View Athlete Report");
            System.out.println("7. View Event Participants");
            System.out.println("8. Sort Event Participants");
            System.out.println("9. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        registerAthlete();
                        break;
                    case 2:
                        registerOrganizer();
                        break;
                    case 3:
                        createEvent();
                        break;
                    case 4:
                        registerForEvent();
                        break;
                    case 5:
                        addAchievement();
                        break;
                    case 6:
                        viewAthleteReport();
                        break;
                    case 7:
                        viewEventParticipants();
                        break;
                    case 8:
                        sortEventParticipants();
                        break;
                    case 9:
                        System.out.println("Exiting system.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void registerAthlete() {
        System.out.print("Enter Athlete ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Athlete Name: ");
        String name = scanner.nextLine();
        athletes.put(id, new Athlete(id, name));
        System.out.println("Athlete registered.");
    }

    private static void registerOrganizer() {
        System.out.print("Enter Organizer ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Organizer Name: ");
        String name = scanner.nextLine();
        organizers.put(id, new Organizer(id, name));
        System.out.println("Organizer registered.");
    }

    private static void createEvent() throws InvalidDateException {
        System.out.print("Enter Event Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Event Date (YYYY-MM-DD): ");
        LocalDate date = parseDate(scanner.nextLine());
        System.out.print("Enter Event Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Event Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        eventManager.createEvent(name, date, location, capacity);
        System.out.println("Event created.");
    }

    private static void registerForEvent() throws EventFullException, DuplicateRegistrationException {
        System.out.print("Enter Athlete ID: ");
        String athId = scanner.nextLine();
        Athlete athlete = athletes.get(athId);
        if (athlete == null) {
            System.out.println("Athlete not found.");
            return;
        }
        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();
        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        eventManager.registerAthleteForEvent(athlete, event);
        System.out.println("Registered for event.");
    }

    private static void addAchievement() throws DuplicateAchievementException, InvalidDateException {
        System.out.print("Enter Athlete ID: ");
        String athId = scanner.nextLine();
        Athlete athlete = athletes.get(athId);
        if (athlete == null) {
            System.out.println("Athlete not found.");
            return;
        }
        System.out.print("Enter Achievement Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Event Name for Achievement: ");
        String eventName = scanner.nextLine();
        System.out.print("Enter Achievement Date (YYYY-MM-DD): ");
        LocalDate date = parseDate(scanner.nextLine());
        System.out.print("Enter Award Level: ");
        String level = scanner.nextLine();
        awardManager.addAchievementToAthlete(athlete, title, eventName, date, level);
        System.out.println("Achievement added.");
    }

    private static void viewAthleteReport() {
        System.out.print("Enter Athlete ID: ");
        String athId = scanner.nextLine();
        Athlete athlete = athletes.get(athId);
        if (athlete == null) {
            System.out.println("Athlete not found.");
            return;
        }
        athlete.generateReport();
    }

    private static void viewEventParticipants() {
        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();
        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        eventManager.searchParticipantsByEvent(event);
    }

    private static void sortEventParticipants() {
        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();
        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        eventManager.sortParticipantsByEvent(event);
    }

    private static LocalDate parseDate(String dateStr) throws InvalidDateException {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid date format.");
        }
    }

    private static Event findEventByName(String name) {
        for (Event ev : eventManager.getEvents()) {
            if (ev.getName().equalsIgnoreCase(name)) {
                return ev;
            }
        }
        return null;
    }
}