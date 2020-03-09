import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { JudicialProcessType } from './judicial-process-type.model';
import { JudicialProcessTypeService } from './judicial-process-type.service';

@Injectable()
export class JudicialProcessTypePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private judicialProcessTypeService: JudicialProcessTypeService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.judicialProcessTypeService.find(id)
                    .subscribe((judicialProcessTypeResponse: HttpResponse<JudicialProcessType>) => {
                        const judicialProcessType: JudicialProcessType = judicialProcessTypeResponse.body;
                        this.ngbModalRef = this.judicialProcessTypeModalRef(component, judicialProcessType);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.judicialProcessTypeModalRef(component, new JudicialProcessType());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    judicialProcessTypeModalRef(component: Component, judicialProcessType: JudicialProcessType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.judicialProcessType = judicialProcessType;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
