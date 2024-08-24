describe('Not found page', () => {
  it('should display "Page not found!"', () => {
    cy.visit('/not-foundable-page', { failOnStatusCode: false });
    cy.url().should('include', '404');
    cy.get('h1').should('contain', 'Page not found !');
  });
});
