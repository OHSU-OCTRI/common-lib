/**
 * Ensure form is cleared when navigating through back button
 */
(function () {
  'use strict';

  window.addEventListener('pageshow', function (event) {
    const forms = document.querySelectorAll('form');
    for (const form of forms) {
      // Clear the form
      form.reset();

      // Re-enable the submit button if needed
      const submitButton = form.querySelector('[type="submit"]');
      if (submitButton) {
        submitButton.disabled = false;
      }
    }
  });
})();