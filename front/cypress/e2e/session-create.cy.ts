describe('Session creation', () => {
    beforeEach(() => {
        cy.login(true)
        cy.intercept('GET', '/api/teacher', {
            body: [
              { id: 1, firstName: 'John', lastName: 'Doe' },
              { id: 2, firstName: 'Jane', lastName: 'Smith' }
            ]
        }).as('getTeachers');
        cy.intercept('GET', '/api/session', {
            body: {
            id: 1,
            name: 'session name',
            date: '2024-08-24T13:27:22.000+00:00',
            teacher_id: 1,
            description: 'Session description',
            users: [2],
            createdAt: '2024-08-24T13:27:22.000+00:00',
            updatedAt: '2024-08-24T13:27:22.000+00:00',
            }
        }).as('getSessions');
    })
    it('should a create button then display creation form', () => {
        cy.get('button.mat-raised-button').click()
        cy.get('form').should('be.visible');

        cy.url().should('include', '/sessions/create')

        cy.intercept('POST', '/api/session', {
            body: {
                id: 1,
                name: "session 1",
                date: "2012-01-01T00:00:00.000+00:00",
                teacher_id: 1,
                description: "my description",
                users: [],
                createdAt: "2024-08-24T18:10:31.965733",
                updatedAt: "2024-08-24T18:10:32.003619"
            },
        }).as('createSession')

        cy.get('input[formControlName=name]').type("name")
        cy.get('input[formControlName=date]').type(`2024-01-01`)
        cy.get('mat-select[formControlName=teacher_id]').click()
        cy.get('mat-option').contains('John Doe').click();
        cy.get('textarea[formControlName=description]').type("Description")
        cy.get('button.mat-raised-button').click()

        cy.url().should('include', '/sessions')
        cy.get('.mat-snack-bar-container').should('contain', 'Session created !');
    })
})