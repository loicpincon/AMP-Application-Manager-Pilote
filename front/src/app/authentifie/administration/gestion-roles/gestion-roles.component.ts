import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { runInThisContext } from 'vm';
import { User } from '../modele/model';

@Component({
  selector: 'app-gestion-roles',
  templateUrl: './gestion-roles.component.html',
  styleUrls: ['./gestion-roles.component.css']
})
export class GestionRolesComponent implements OnInit {

  rights = new Array();

  roles = new Array();

  newRole: string;

  users: User[]



  constructor(private apmService: ApmService) { }

  ngOnInit(): void {
    this.apmService.recupererAllUser().subscribe(users => {
      this.users = users;
      this.users.forEach(user => {
        user.roles = []
      })
    })
    this.rights = [{
      id: 'AppConsult', description: 'consultation'
    },
    {
      id: 'AppUpdate', description: 'modification'
    }
    ];


  }

  ajouterRoles() {
    this.roles.push({ id: this.newRole, rights: [] })
    this.newRole = null;
  }


  assignRoleToUser(role, user) {
    let add = true;
    user.roles.forEach(element => {
      if (element.id == role.id) {
        const index = user.roles.indexOf(element);
        user.roles.splice(index, 1);
        add = false
      };
    });
    if (add) {
      user.roles.push(role)
    }
    console.log(user.roles.length)
  }



  assignRightToRole(role, right) {
    let add = true;
    role.rights.forEach(element => {
      if (element.id == right.id) {
        const index = role.rights.indexOf(element);
        role.rights.splice(index, 1);
        add = false
      };
    });
    if (add) {
      role.rights.push(right)
    }
    console.log(role)
  }

}
