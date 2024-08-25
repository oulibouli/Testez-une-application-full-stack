describe('Session management', () => {
    let sessions = [
        {
            "id": 1,
            "name": "Test",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test",
            "users": [],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        },
        {
            "id": 2,
            "name": "Test 2",
            "date": "2024-07-25T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Test 2",
            "users": [],
            "createdAt": "2024-07-19T11:30:09",
            "updatedAt": "2024-07-22T16:42:27"
        }
    ]
    beforeEach(() => {
        cy.intercept('GET', '/api/teacher', {
            body: [
              { id: 1, firstName: 'John', lastName: 'Doe' },
              { id: 2, firstName: 'Jane', lastName: 'Smith' }
            ]
        }).as('getTeachers');

        cy.intercept('GET', '/api/teacher/1', {
            id: 1, firstName: 'John', lastName: 'Doe' 
        }).as('getTeacher');
        cy.intercept('GET', '/api/session', sessions);

        cy.login(true)
    })
    it('should create button then display creation form', () => {
        sessions.push({
            "id": 3,
            "name": "name",
            "date": "2024-01-01T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Description",
            "users": [],
            "createdAt": "2024-01-01T11:30:09",
            "updatedAt": "2024-01-01T16:42:27"
        });
        cy.contains('button', 'Create').click();
        cy.get('form').should('be.visible');

        cy.url().should('include', '/sessions/create')

        cy.intercept('POST', '/api/session', {
            body: {
                id: 3,
                name: "name",
                date: "2024-01-01T00:00:00.000+00:00",
                teacher_id: 1,
                description: "Description",
                users: [],
                createdAt: "2024-01-01T11:30:09",
                updatedAt: "2024-01-01T16:42:27"
            },
        }).as('createSession')

        cy.get('input[formControlName=name]').type("name")
        cy.get('input[formControlName=date]').type(`2024-01-01`)
        cy.get('mat-select[formControlName=teacher_id]').click()
        cy.get('mat-option').contains('John Doe').click();
        cy.get('textarea[formControlName=description]').type("Description")

        cy.intercept('GET', '/api/session', sessions);
        cy.contains('button', 'Save').click();

        cy.url().should('include', '/sessions')
        cy.get('.mat-snack-bar-container').should('contain', 'Session created !');
    })

    it('should let the admin delete a session', () => {
        cy.intercept('DELETE', '/api/session/1', {
            statusCode:200
        })
        cy.intercept('GET', '/api/session', {
            body: [{
                "id": 2,
                "name": "Test 2",
                "date": "2024-07-25T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "Test 2",
                "users": [],
                "createdAt": "2024-07-19T11:30:09",
                "updatedAt": "2024-07-22T16:42:27"
            },{
                id: 3,
                name: "name",
                date: "2024-01-01T00:00:00.000+00:00",
                teacher_id: 1,
                description: "Description",
                users: [],
                createdAt: "2024-01-01T11:30:09",
                updatedAt: "2024-01-01T16:42:27"
            }]
        })
        cy.intercept('GET', '/api/session/1', sessions[0]).as('sessionDetail');
        cy.get('button').contains('Detail').click()
        cy.get('button').contains('Delete').click()

        cy.get('.mat-snack-bar-container').should('contain', 'Session deleted !');

        cy.url().should('include', '/session');
    })
    it('should let the admin update a session', () => {
        const sessionUpdated = {
            id:1,
            name: "session updated",
            date: "2024-08-27T00:00:00.000+00:00",
            teacher_id: 1,
            description: "updated description",
            users: [],
            createdAt: "2024-07-19T11:30:09",
            updatedAt: "2024-08-25T16:42:27"
        }

        cy.intercept('GET', '/api/session', {
            body: [sessionUpdated,
                {
                    "id": 2,
                    "name": "Test 2",
                    "date": "2024-07-25T00:00:00.000+00:00",
                    "teacher_id": 1,
                    "description": "Test 2",
                    "users": [],
                    "createdAt": "2024-07-19T11:30:09",
                    "updatedAt": "2024-07-22T16:42:27"
                },{
                id: 3,
                name: "name",
                date: "2024-01-01T00:00:00.000+00:00",
                teacher_id: 1,
                description: "Description",
                users: [],
                createdAt: "2024-01-01T11:30:09",
                updatedAt: "2024-01-01T16:42:27"
            }]
        }).as('updatedSessions')
        cy.intercept('PUT', '/api/session/1', {
            statusCode:200,
            body: [sessionUpdated]
        })

        cy.intercept('GET', '/api/session/1', sessions[0]).as('sessionDetail');

        cy.get('button').contains('Edit').click()

        cy.get('input[formControlName=name]').type(" - session updated")
        cy.get('input[formControlName=date]').type(`2024-08-25`)
        cy.get('mat-select[formControlName=teacher_id]').click()
        cy.get('mat-option').contains('John Doe').click();
        cy.get('textarea[formControlName=description]').type(" - updated description")
        cy.get('button').contains('Save').click()

        
        

    })
})