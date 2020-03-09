/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { JudicialProcessTypeDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type-delete-dialog.component';
import { JudicialProcessTypeService } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.service';

describe('Component Tests', () => {

    describe('JudicialProcessType Management Delete Component', () => {
        let comp: JudicialProcessTypeDeleteDialogComponent;
        let fixture: ComponentFixture<JudicialProcessTypeDeleteDialogComponent>;
        let service: JudicialProcessTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [JudicialProcessTypeDeleteDialogComponent],
                providers: [
                    JudicialProcessTypeService
                ]
            })
            .overrideTemplate(JudicialProcessTypeDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JudicialProcessTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JudicialProcessTypeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
