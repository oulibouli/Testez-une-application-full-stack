describe('Sessions details', () => {
    const session = [
        {
            "id": 1,
            "name": "Test",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test",
            "users": [5],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        }
    ]
    beforeEach(() => {
        cy.intercept('GET', '/api/session', session).as('sessions');
        cy.intercept('GET', '/api/teacher/1', {
            id: 1, firstName: 'John', lastName: 'Doe' 
        }).as('getTeachers');
        
        cy.login(false)
    })
    it('should display the session details', () => {
        cy.intercept('GET', '/api/session/1', session[0]).as('sessionDetail');
        cy.get('button').contains('Detail').click()

        cy.get('h1').should('contain', 'Test');
        cy.get('h1').should('contain', 'Test');
        cy.get('.ml1').should('contain', '1 attendees');
        cy.get('.ml1').should('contain', 'July 25, 2024');
        cy.get('.description').should('contain', 'Description: Test');
        cy.get('.created').should('contain', 'Create at:  July 19, 2024');
        cy.get('.updated').should('contain', 'Last update:  July 22, 2024');
    })
    
    it('should let the user participate a session', () => {
        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode:200
        })
        cy.intercept('GET', '/api/session/1', session[0]).as('sessionDetail');
        cy.get('button').contains('Detail').click()
        cy.get('button').contains('Participate').click()
    })
    it('should let the user unparticipate a session', () => {
        const session = [
            {
                "id": 1,
                "name": "Test",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test",
                "users": [1],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            }
        ]
        cy.intercept('GET', '/api/session', session)
        cy.login(false)
        cy.intercept('POST', '/api/session/1/unparticipate/1', {
            statusCode:200
        })
        cy.intercept('GET', '/api/session/1', session[0]).as('sessionDetail');
        cy.get('button').contains('Detail').click()
        cy.get('button').contains('Do not participate').click()
    })
})