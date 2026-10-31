Output :
- Setup complete: offerings [CS101, CS201], learners [L1, L2]
- enroll(CS201, L2) -> REQUIREMENTS_NOT_MET
- enroll(CS101, L1) -> SUCCESSFULLY_ENROLLED
- enroll(CS101, L2) -> SUCCESSFULLY_ENROLLED
- enroll(CS101, L2) duplicate -> ALREADY_ENROLLED
- registerLearner(L3)
- enroll(CS101, L3) -> ADDED_TO_WAITLIST
- drop(CS101, L1) -> DROPPED_SUCCESSFULLY, promoted=L3
Smoke tests passed
