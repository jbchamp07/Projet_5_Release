describe('SessionDetail spec', () => {

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
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')
    });

    it('GetDetailPage successfull', () => {

    // Interception pour le détail de la session
    cy.intercept('GET', '/api/session/1', {
        body: {
            "id": 1,
            "name": "Session 1",
            "date": "2024-06-18T10:00:00Z",
            "teacher_id": 1001,
            "description": "Introduction to yoga.",
            "users": [],
            "createdAt": "2024-01-01T08:00:00",
            "updatedAt": "2024-06-01T08:00:00"
        }
    }).as('getSessionDetail')

    //cy.visit('/sessions/detail/1');
    // Cliquer sur le bouton "Détail" pour la session avec ID 1
    cy.contains('button', 'Detail').click();

    // Attendre que la page de détail se charge et vérifier le contenu
    cy.url().should('include', '/sessions/detail/1');
    //cy.wait('@getSessionDetail');
    
    // Vérifie le titre de la session
    cy.get('h1').should('contain.text', 'Session 1');

    // Vérifie la date de la session
    cy.get('.ml1').should('contain.text', 'June 18, 2024');
    
    // Vérifie la description de la session
    cy.get('.description').should('contain.text', 'Introduction to yoga.');
  

    })

});