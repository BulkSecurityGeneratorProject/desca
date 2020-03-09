/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { JudicialProcessTypeDialogComponent } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type-dialog.component';
import { JudicialProcessTypeService } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.service';
import { JudicialProcessType } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.model';

describe('Component Tests', () => {

    describe('JudicialProcessType Management Dialog Component', () => {
        let comp: JudicialProcessTypeDialogComponent;
        let fixture: ComponentFixture<JudicialProcessTypeDialogComponent>;
        let service: JudicialProcessTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [JudicialProcessTypeDialogComponent],
                providers: [
                    JudicialProcessTypeService
                ]
            })
            .overrideTemplate(JudicialProcessTypeDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JudicialProcessTypeDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JudicialProcessTypeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new JudicialProcessType(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.judicialProcessType = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'judicialProcessTypeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new JudicialProcessType();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.judicialProcessType = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'judicialProcessTypeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
