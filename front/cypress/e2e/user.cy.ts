describe('User spec', () => {

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

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []).as('session')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')
    });

    it('Register successfull', () => {
        cy.get('.app span.link').contains('Logout').click();
        cy.visit('/register')
    
        // cy.intercept('POST', '/api/auth/register', {
        //   body: {
        //     id: 1,
        //     username: 'userName',
        //     firstName: 'firstName',
        //     lastName: 'lastName',
        //     admin: true
        //   },
        // })
    
        // cy.intercept(
        //   {
        //     method: 'GET',
        //     url: '/api/auth/login',
        //   },
        //   []).as('login')
    
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.get('input[formControlName=firstName]').type("first")
        cy.get('input[formControlName=lastName]').type(`${"last"}{enter}{enter}`)
    
        cy.url().should('include', '/login')
      })

      it('Register fail email already taken', () => {
        cy.get('.app span.link').contains('Logout').click();
        cy.visit('/register')
    
        // cy.intercept('POST', '/api/auth/register', {
        //   body: {
        //     id: 1,
        //     username: 'userName',
        //     firstName: 'firstName',
        //     lastName: 'lastName',
        //     admin: true
        //   },
        // })
    
        // cy.intercept(
        //   {
        //     method: 'GET',
        //     url: '/api/auth/login',
        //   },
        //   []).as('login')
    
        cy.get('input[formControlName=email]').type("yoga@studio.com2")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.get('input[formControlName=firstName]').type("first")
        cy.get('input[formControlName=lastName]').type(`${"last"}{enter}{enter}`)
    
        cy.url().should('include', '/login')
      })

    //User informations
    it('User Information', () => {

        cy.get('span[routerLink="me"]').click();
        cy.url().should('include', '/me');

    cy.get('.m3 mat-card').should('exist');

    cy.get('.m3 mat-card p').contains('Name: first LAST').should('exist');

    cy.get('.m3 mat-card p').contains('Email: yoga@studio.com').should('exist');

    cy.get('.m3 mat-card p.my2').contains('You are admin').should('exist');

    cy.get('.m3 mat-card p').contains('Create at:').should('exist');

    cy.get('.m3 mat-card p').contains('Last update:').should('exist');
        
    })


});