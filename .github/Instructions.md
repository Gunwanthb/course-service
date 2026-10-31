Act as a Senior Backend Java Engineer. Implement `CourseRegistrationService.java` in package `com.registration.service`.

Requirements:
1. Inject `InMemoryCourseRepository`.
2. Implement `RegistrationResult enroll(String courseCode, String learnerId)`:
   - Validate existence of course and learner.
   - Enforce prerequisite check: if learner lacks required prerequisites, return PREREQUISITE_NOT_MET without modifying state.
   - Idempotency / Duplicate checks: if learner is already enrolled, return ALREADY_ENROLLED; if already waitlisted, return ALREADY_ON_WAITLIST.
   - If capacity is available, enroll the learner and return SUCCESSFULLY_ENROLLED.
   - If full, add the learner to the FIFO waitlist and return ADDED_TO_WAITLIST.
3. Implement `RegistrationResult drop(String courseCode, String learnerId)`:
   - Validate existence of course and learner.
   - If learner is not currently enrolled, return NOT_ENROLLED without modifying state.
   - Remove learner from enrolled set.
   - Implement FIFO waitlist promotion policy (Skip and Evict): inspect waitlist in FIFO order; verify if the next candidate still meets prerequisites. If yes, promote them to enrolled. If not eligible, evict them and inspect the next candidate until an eligible learner is promoted or the queue is empty.
   - Return DROPPED_SUCCESSFULLY with the promoted learner ID (if any).
4. Concurrency: Use the `ReentrantLock` from `CourseOffering` to serialize mutations per offering.