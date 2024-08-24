describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login')
  })

  it('should display login form', () => {
    cy.get('form').should('be.visible')
  })
  it('should display error on incorrect login', () => {
    // Soumettre le formulaire avec des informations incorrectes
    cy.get('input[formControlName="email"]').type('invalidemail');
    cy.get('input[formControlName="password"]').type(`${"tqdqs"}{enter}{enter}`);
    
    // Vérifier que le message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred');
  });
  it('Login successfull', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});