describe('AuthGuard E2E Tests', () => {
    beforeEach(() => {
      // Vous pouvez ici préparer votre application pour le test
      cy.visit('/'); // Par exemple, accéder à la page d'accueil
    });
  
    it('should redirect to login if the user is not logged in', () => {
      // Mock de la réponse du service de session
      cy.intercept('GET', '**/session', { isLogged: false }).as('checkSession');
  
      // Visiter une route protégée
      cy.visit('/sessions'); // remplacez par votre route protégée
  
      // Vérifiez que l'utilisateur a été redirigé vers la page de login
      cy.url().should('include', '/login');
    });
  
    it('should allow navigation if the user is logged in', () => {
        cy.login(false);
  
      // Visit a protected route
      cy.visit('/sessions');
  

    });
  });