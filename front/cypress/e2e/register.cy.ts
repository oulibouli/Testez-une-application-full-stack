describe('Register spec', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display register form', () => {
    cy.get('form').should('be.visible');
  });

  it('should display an error when register form fails', () => {
    cy.get('input[formControlName=firstName]').type("firstname");
    cy.get('input[formControlName=lastName]').type("lastname");
    cy.get('input[formControlName=email]').type("email@email.com");
    cy.get('input[formControlName=password]').type("pas{enter}{enter}");

    cy.get('.error').should('contain', 'An error occurred');
  });

  it('should register successfully', () => {
    cy.intercept('POST', '/api/auth/register', {
      body: {
        message: "User registered successfully!"
      }
    });

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      }
    });

    cy.get('input[formControlName=firstName]').type("firstname");
    cy.get('input[formControlName=lastName]').type("lastname");
    cy.get('input[formControlName=email]').type("email@email.com");
    cy.get('input[formControlName=password]').type("password{enter}{enter}");

    cy.url().should('include', '/login');
  });
});
