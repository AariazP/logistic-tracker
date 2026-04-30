
# TestSprite AI Testing Report(MCP)

---

## 1️⃣ Document Metadata
- **Project Name:** logistic-tracker-front
- **Date:** 2026-04-30
- **Prepared by:** TestSprite AI Team

---

## 2️⃣ Requirement Validation Summary

#### Test TC001 Log in and reach the package board with status columns visible
- **Test Code:** [TC001_Log_in_and_reach_the_package_board_with_status_columns_visible.py](./TC001_Log_in_and_reach_the_package_board_with_status_columns_visible.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/6170eb48-646b-400e-aec6-1a00d6427d2f
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC002 View packages grouped by status on the board
- **Test Code:** [TC002_View_packages_grouped_by_status_on_the_board.py](./TC002_View_packages_grouped_by_status_on_the_board.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/7cde5cc2-75cc-41f7-98d7-99b7d14cbc55
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC003 Move package from RECEIVED to IN_TRANSIT via drag and drop
- **Test Code:** [TC003_Move_package_from_RECEIVED_to_IN_TRANSIT_via_drag_and_drop.py](./TC003_Move_package_from_RECEIVED_to_IN_TRANSIT_via_drag_and_drop.py)
- **Test Error:** TEST FAILURE

The dispatcher account cannot perform the drag-and-drop action — the feature is restricted to DRIVER users and no package card was available to move.

Observations:
- The page displays the message: 'Drag and drop is available only for users with the DRIVER role.'
- The RECEIVED, IN TRANSIT, and DELIVERED columns all show 0 and contain no package cards to drag.

- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/6de989a9-b8af-4f05-a5f9-186ba613ee11
- **Status:** ❌ Failed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC004 Admin can view recipients and drivers lists
- **Test Code:** [TC004_Admin_can_view_recipients_and_drivers_lists.py](./TC004_Admin_can_view_recipients_and_drivers_lists.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/1f849fdf-0391-454b-99bd-7f2276a12d14
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC005 Filter board by status and then clear filters to restore full board
- **Test Code:** [TC005_Filter_board_by_status_and_then_clear_filters_to_restore_full_board.py](./TC005_Filter_board_by_status_and_then_clear_filters_to_restore_full_board.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/4835788f-b11e-4a5f-94ca-c2f388e71622
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC006 Search by tracking ID and clear search to restore full board
- **Test Code:** [TC006_Search_by_tracking_ID_and_clear_search_to_restore_full_board.py](./TC006_Search_by_tracking_ID_and_clear_search_to_restore_full_board.py)
- **Test Error:** TEST BLOCKED

The feature cannot be tested because required prerequisite data (recipients and packages) cannot be created through the available UI, preventing a valid tracking ID search.

Observations:
- Navigating to /manage returned a 404 error when attempting to open the Manage drivers and recipients page.
- The recipient dropdown shows only 'Select recipient' and contains no recipients.
- The board shows zero packages in all lanes, so there is no tracking ID available to search for.
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/5fc29453-bba5-4792-90b2-1915c1ef11c2
- **Status:** BLOCKED
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC007 Create a package from the board and see it appear on the board
- **Test Code:** [TC007_Create_a_package_from_the_board_and_see_it_appear_on_the_board.py](./TC007_Create_a_package_from_the_board_and_see_it_appear_on_the_board.py)
- **Test Error:** TEST FAILURE

The app did not allow selecting a recipient when registering a package. The recipient dropdown remained empty even after creating a recipient and using the Refresh recipients control.

Observations:
- The recipient dropdown only shows 'Select recipient' and no recipient options are available.
- A recipient was created in Manage drivers and recipients but it did not become selectable after returning to the board.
- Clicking 'Refresh recipients' multiple times did not populate the recipient list.
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/583267a2-a55a-4f75-bc28-36937d09779e
- **Status:** ❌ Failed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC008 Admin can create a recipient and see it in the list
- **Test Code:** [TC008_Admin_can_create_a_recipient_and_see_it_in_the_list.py](./TC008_Admin_can_create_a_recipient_and_see_it_in_the_list.py)
- **Test Error:** TEST FAILURE

Creating a recipient did not show a success confirmation or add the new recipient to the recipients list.

Observations:
- The recipients form remained filled with the submitted values after clicking 'Create recipient' and no success message appeared.
- There is no recipient entry visible in the recipients list area; only a driver entry is shown on the page.
- No UI confirmation or new list item was observed within the recipients panel after submission.
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/ff0f965f-5e17-48ce-8bfe-c4b574acc121
- **Status:** ❌ Failed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC009 Admin can create a driver and see it in the drivers list
- **Test Code:** [TC009_Admin_can_create_a_driver_and_see_it_in_the_drivers_list.py](./TC009_Admin_can_create_a_driver_and_see_it_in_the_drivers_list.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/bd9717a9-51de-4f6d-a2ee-6e99d53c8901
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC010 Admin can edit an existing recipient and see updates reflected
- **Test Code:** [TC010_Admin_can_edit_an_existing_recipient_and_see_updates_reflected.py](./TC010_Admin_can_edit_an_existing_recipient_and_see_updates_reflected.py)
- **Test Error:** TEST FAILURE

Editing a recipient could not be completed because the recipients list or edit controls are not available after creating a recipient.

Observations:
- The Recipients creation form is present and filled, but no created recipient entry appeared in a recipients list after clicking 'Create recipient'.
- There are no edit buttons or recipient items visible on the Admin page to open an edit view.
- The Drivers section shows an entry and a Delete button, indicating list/edit controls exist for drivers but not for recipients.
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/4d7e2b5d-8b40-45bf-ab28-0663f39602ee
- **Status:** ❌ Failed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC011 Package creation form can be opened and recipient options are available
- **Test Code:** [TC011_Package_creation_form_can_be_opened_and_recipient_options_are_available.py](./TC011_Package_creation_form_can_be_opened_and_recipient_options_are_available.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/910144f4-38d0-4508-b037-fd92e9468289
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC012 Admin can delete a recipient after confirming
- **Test Code:** [TC012_Admin_can_delete_a_recipient_after_confirming.py](./TC012_Admin_can_delete_a_recipient_after_confirming.py)
- **Test Error:** TEST BLOCKED

The feature could not be reached — the admin recipients page is not accessible.

Observations:
- Navigating to http://localhost:4200/admin returned a 404 error page with message 'File not found.'
- The page shows 'Error code: 404' and 'HTTPStatus.NOT_FOUND - Nothing matches the given URI.'

- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/f1d06ab3-3cfd-4d82-b4d3-293a55012c10
- **Status:** BLOCKED
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC013 Admin can delete a driver after confirming
- **Test Code:** [TC013_Admin_can_delete_a_driver_after_confirming.py](./TC013_Admin_can_delete_a_driver_after_confirming.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/54b04f60-ccb2-4018-a13d-7be985937ff2
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC014 Package creation requires tracking ID
- **Test Code:** [TC014_Package_creation_requires_tracking_ID.py](./TC014_Package_creation_requires_tracking_ID.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/a8549cc7-f6c5-4114-8a81-e22d48edc751
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC015 Prevent invalid move from DELIVERED to RECEIVED
- **Test Code:** [TC015_Prevent_invalid_move_from_DELIVERED_to_RECEIVED.py](./TC015_Prevent_invalid_move_from_DELIVERED_to_RECEIVED.py)
- **Test Error:** TEST BLOCKED

The drag-and-drop scenario could not be executed because the feature is restricted to DRIVER users and the current account is an ADMIN.

Observations:
- The page displays the message: 'Drag and drop is available only for users with the DRIVER role.'
- The current user is shown as 'admin' with the ADMIN role, so drag-and-drop cannot be attempted.
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/0827e892-0b74-4e4e-9c0b-01bc39471319
- **Status:** BLOCKED
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC016 Reject login with incorrect credentials
- **Test Code:** [TC016_Reject_login_with_incorrect_credentials.py](./TC016_Reject_login_with_incorrect_credentials.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/4fc71781-1387-4aff-ab26-f78871052de3
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---

#### Test TC017 Validate required fields on empty login submit
- **Test Code:** [TC017_Validate_required_fields_on_empty_login_submit.py](./TC017_Validate_required_fields_on_empty_login_submit.py)
- **Test Visualization and Result:** https://www.testsprite.com/dashboard/mcp/tests/50be865e-8d84-42ef-b91e-9c9d6f7d9ef1/2d81b784-c866-48da-a08d-b2cbcd0fbec9
- **Status:** ✅ Passed
- **Analysis / Findings:** {{TODO:AI_ANALYSIS}}.
---


## 3️⃣ Coverage & Matching Metrics

- **58.82** of tests passed

| Requirement        | Total Tests | ✅ Passed | ❌ Failed  |
|--------------------|-------------|-----------|------------|
| ...                | ...         | ...       | ...        |
---


## 4️⃣ Key Gaps / Risks
{AI_GNERATED_KET_GAPS_AND_RISKS}
---