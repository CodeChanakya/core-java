package com.codechanakya.scheduler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class Main {

    private static final ZoneId NEW_YORK = ZoneId.of("America/New_York");
    private static final ZoneId LONDON = ZoneId.of("Europe/London");
    private static final ZoneId KOLKATA = ZoneId.of("Asia/Kolkata");

    public static void main(String[] args) throws IOException {
        System.out.println("--- One moment, three clocks ---");
        ZonedDateTime standup = ZonedDateTime.of(2026, 3, 9, 10, 0, 0, 0, NEW_YORK);
        System.out.println("  New York: " + standup);
        System.out.println("  London  : " + standup.withZoneSameInstant(LONDON));
        System.out.println("  Kolkata : " + standup.withZoneSameInstant(KOLKATA));
        Instant moment = standup.toInstant();
        System.out.println("  Same Instant in UTC: " + moment);

        System.out.println();
        System.out.println("--- Booking across zones ---");
        Scheduler scheduler = new Scheduler();
        book(scheduler, new Appointment("Stand-up", standup, Duration.ofMinutes(30)));
        book(scheduler, new Appointment("Design review",
                ZonedDateTime.of(2026, 3, 9, 14, 15, 0, 0, LONDON), Duration.ofMinutes(45)));
        book(scheduler, new Appointment("Client call",
                ZonedDateTime.of(2026, 3, 9, 20, 30, 0, 0, KOLKATA), Duration.ofMinutes(60)));
        book(scheduler, new Appointment("Retro",
                ZonedDateTime.of(2026, 3, 10, 15, 0, 0, 0, NEW_YORK), Duration.ofMinutes(60)));

        System.out.println();
        System.out.println("--- The same agenda on two clocks ---");
        System.out.println("  New York:");
        for (String line : scheduler.agendaIn(NEW_YORK)) {
            System.out.println("    " + line);
        }
        System.out.println("  Kolkata:");
        for (String line : scheduler.agendaIn(KOLKATA)) {
            System.out.println("    " + line);
        }

        System.out.println();
        System.out.println("--- Clock changes ---");
        ZonedDateTime missing = ZonedDateTime.of(2026, 3, 8, 2, 30, 0, 0, NEW_YORK);
        System.out.println("  2:30 on 8 March does not exist in New York, so Java gives: " + missing);
        ZonedDateTime repeated = ZonedDateTime.of(2026, 11, 1, 1, 30, 0, 0, NEW_YORK);
        System.out.println("  1:30 on 1 November happens twice. Default (earlier): " + repeated);
        System.out.println("  The later one: " + repeated.withLaterOffsetAtOverlap());

        ZonedDateTime saturday = ZonedDateTime.of(2026, 3, 7, 9, 0, 0, 0, NEW_YORK);
        System.out.println("  Saturday 09:00 plusDays(1)       : " + saturday.plusDays(1));
        System.out.println("  Saturday 09:00 plus 24 hours     : " + saturday.plus(Duration.ofHours(24)));
        LocalDate dstDay = LocalDate.of(2026, 3, 8);
        Duration lengthOfDay = Duration.between(dstDay.atStartOfDay(NEW_YORK), dstDay.plusDays(1).atStartOfDay(NEW_YORK));
        System.out.println("  Hours in 8 March in New York     : " + lengthOfDay.toHours());

        System.out.println();
        System.out.println("--- Exercise answers ---");
        book(scheduler, new Appointment("Vendor sync",                                      // B1
                ZonedDateTime.of(2026, 3, 11, 9, 0, 0, 0, LONDON), Duration.ofMinutes(30)));
        for (Appointment a : scheduler.all()) {                                             // B2
            System.out.println("  " + a.getTitle() + " ends " + a.getEnd());
        }
        Duration wait = Duration.between(scheduler.all().get(0).getStart(), scheduler.all().get(2).getStart());   // B3
        System.out.println("  From Stand-up to Retro (B3): " + wait.toDays() + " day " + wait.toHoursPart() + " hours");
        System.out.println("  Appointments on 11 March in Kolkata (I1): "
                + scheduler.on(LocalDate.of(2026, 3, 11), KOLKATA).size());
        System.out.println("  Cancel Vendor sync (I2): " + scheduler.cancel("vendor sync")
                + ", again: " + scheduler.cancel("vendor sync"));
        ZonedDateTime free = scheduler.firstFreeSlot(standup, Duration.ofMinutes(45));      // C1
        System.out.println("  First free 45 minutes from the stand-up (C1): " + free);

        System.out.println();
        System.out.println("--- Saving and loading ---");
        Path work = Path.of("out", "work");
        Files.createDirectories(work);
        Path file = work.resolve("appointments.txt");
        scheduler.save(file);
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            System.out.println("  | " + line);
        }
        Scheduler loaded = Scheduler.load(file);
        System.out.println("  Loaded " + loaded.all().size() + " appointments, "
                + loaded.getLoadProblems().size() + " problems");
        boolean same = true;
        for (int i = 0; i < loaded.all().size(); i++) {
            same &= loaded.all().get(i).getStart().equals(scheduler.all().get(i).getStart());
        }
        System.out.println("  Every start time survived the round trip: " + same);

        System.out.println();
        System.out.println("--- A damaged file ---");
        List<String> damage = List.of(
                "Lunch|2026-03-11T12:00-04:00[America/New_York]|45",
                "this line is not an appointment",
                "Planning|2026-03-11T13:00-04:00[Mars/Olympus_Mons]|30",
                "Overlap|2026-03-09T10:10-04:00[America/New_York]|20",
                "Late|2026-03-11T16:00-04:00[America/New_York]|abc");
        Files.write(file, damage, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        Scheduler damaged = Scheduler.load(file);
        System.out.println("  Loaded " + damaged.all().size() + " appointments");
        for (String problem : damaged.getLoadProblems()) {
            System.out.println("  Skipped " + problem);
        }
    }

    private static void book(Scheduler scheduler, Appointment appointment) {
        try {
            scheduler.add(appointment);
            System.out.println("  Booked  \"" + appointment.getTitle() + "\"");
        } catch (ScheduleConflictException e) {
            System.out.println("  Refused " + e.getMessage());
        }
    }
}
