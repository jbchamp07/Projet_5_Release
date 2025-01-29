describe('Session spec', () => {

    beforeEach(() => {
        //Login
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        // cy.intercept(
        //     {
        //         method: 'GET',
        //         url: '/api/session',
        //     },
        //     []).as('session')
        cy.intercept('GET', 'api/session', {
            body: [
                {
                    "id": 1,
                    "name": "Session 1",
                    "date": "2024-06-18T10:00:00Z",
                    "teacher_id": 1001,
                    "description": "Introduction to yoga.",
                    "users": [],
                    "createdAt": "2024-01-01T08:00:00",
                    "updatedAt": "2024-06-01T08:00:00"
                },
                {
                    "id": 2,
                    "name": "Session 2",
                    "date": "2024-06-19T10:00:00Z",
                    "teacher_id": 1002,
                    "description": "For advanced yogis.",
                    "users": [],
                    "createdAt": "2024-01-02T08:00:00",
                    "updatedAt": "2024-06-02T08:00:00"
                }
            ],
        }).as('getSessions')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type("test!1234")
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/sessions')
    });

    //Create session
    it('Create Session', () => {

        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [
                {
                    "id": 1001,
                    "lastName": "Dupont",
                    "firstName": "Jean",
                    "createdAt": "2023-06-18T10:15:30",
                    "updatedAt": "2024-06-18T12:30:45"
                  },
                  {
                    "id": 1002,
                    "lastName": "Martin",
                    "firstName": "Marie",
                    "createdAt": "2023-07-20T14:00:00",
                    "updatedAt": "2024-07-20T15:45:00"
                  } 
            ]
          }).as('getAllTeachers');

        cy.get('button[routerLink="create"]').click();
        cy.url().should('include', '/sessions/create');

        

        cy.get('input[formControlName=name]').type("Session 1")
        cy.get('input[formControlName=date]').type("2024-06-18")
        cy.get('mat-select[formControlName="teacher_id"]').click();
        cy.get('mat-option').contains('Jean Dupont').click();
        cy.get('textarea[formControlName=description]').type(`${"Introduction to yoga"}`)
        cy.get('button[type="submit"]').click();

        cy.on('window:alert', (str) => {
            expect(str).to.equal('Session created !');
        });
        cy.url().should('include', '/sessions')
    })

    //Verify list
    it('List Sessions', () => {


        
        cy.wait('@getSessions').its('response.statusCode').should('eq', 200);

        cy.get('.list .items').should('be.visible');

        cy.get('.list .item').should('have.length.greaterThan', 0);
    

    })

});