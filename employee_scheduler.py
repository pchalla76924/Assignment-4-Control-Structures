import random

DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
SHIFTS = ["Morning", "Afternoon", "Evening"]
MIN_EMPLOYEES = 2
MAX_DAYS = 5
EMPLOYEES = ["Ava", "Ben", "Carlos", "Deepa", "Ethan", "Fatima", "Grace", "Henry", "Isabella", "Jason", "Kiara", "Liam"]


def sample_preferences():
    return {e: {d: SHIFTS[(i + j) % 3] for j, d in enumerate(DAYS)} for i, e in enumerate(EMPLOYEES)}


def collect_preferences():
    preferences = {}
    mode = input("Enter M for manual preferences or press Enter for sample preferences: ").strip().lower()
    if mode != "m":
        return sample_preferences()
    for employee in EMPLOYEES:
        preferences[employee] = {}
        for day in DAYS:
            while True:
                value = input(f"{employee} - {day} (morning/afternoon/evening): ").strip().capitalize()
                if value in SHIFTS:
                    preferences[employee][day] = value
                    break
                print("Invalid shift. Please try again.")
    return preferences


def make_schedule():
    return {day: {shift: [] for shift in SHIFTS} for day in DAYS}


def already_working(schedule, employee, day):
    return any(employee in schedule[day][shift] for shift in SHIFTS)


def can_assign(schedule, days_worked, employee, day):
    return days_worked[employee] < MAX_DAYS and not already_working(schedule, employee, day)


def assign(schedule, days_worked, employee, day, shift):
    if can_assign(schedule, days_worked, employee, day):
        schedule[day][shift].append(employee)
        days_worked[employee] += 1
        return True
    return False


def build_schedule(preferences):
    schedule = make_schedule()
    days_worked = {e: 0 for e in EMPLOYEES}

    # Pass 1: honor preferred shifts while baseline coverage is open.
    for day in DAYS:
        for shift in SHIFTS:
            interested = [e for e in EMPLOYEES if preferences[e][day] == shift and can_assign(schedule, days_worked, e, day)]
            random.shuffle(interested)
            for employee in interested:
                if len(schedule[day][shift]) >= MIN_EMPLOYEES:
                    break
                assign(schedule, days_worked, employee, day, shift)

    # Pass 2: resolve same-day shortages using eligible employees.
    for day in DAYS:
        for shift in SHIFTS:
            while len(schedule[day][shift]) < MIN_EMPLOYEES:
                candidates = [e for e in EMPLOYEES if can_assign(schedule, days_worked, e, day)]
                if not candidates:
                    break
                random.shuffle(candidates)
                candidates.sort(key=lambda e: days_worked[e])
                if not assign(schedule, days_worked, candidates[0], day, shift):
                    break

    return schedule, days_worked


def validate(schedule, days_worked):
    errors = []
    for day in DAYS:
        for shift in SHIFTS:
            if len(schedule[day][shift]) < MIN_EMPLOYEES:
                errors.append(f"{day} {shift} has fewer than {MIN_EMPLOYEES} employees")
    for employee in EMPLOYEES:
        if days_worked[employee] > MAX_DAYS:
            errors.append(f"{employee} exceeds {MAX_DAYS} work days")
        for day in DAYS:
            count = sum(employee in schedule[day][shift] for shift in SHIFTS)
            if count > 1:
                errors.append(f"{employee} has multiple shifts on {day}")
    return errors


def display(schedule, days_worked):
    print("\nFINAL WEEKLY SCHEDULE")
    print("=" * 58)
    for day in DAYS:
        print(f"\n{day}")
        print("-" * 58)
        for shift in SHIFTS:
            print(f"{shift:<12}: {', '.join(schedule[day][shift])}")
    print("\nEMPLOYEE WORKLOAD")
    print("-" * 58)
    for e in EMPLOYEES:
        print(f"{e:<12}: {days_worked[e]} day(s)")


def main():
    random.seed(7)
    print("EMPLOYEE WEEKLY SCHEDULER")
    preferences = collect_preferences()
    schedule, days_worked = build_schedule(preferences)
    display(schedule, days_worked)
    errors = validate(schedule, days_worked)
    print("\nVALIDATION")
    print("-" * 58)
    print("Schedule passed all validation checks." if not errors else "\n".join(errors))


if __name__ == "__main__":
    main()
