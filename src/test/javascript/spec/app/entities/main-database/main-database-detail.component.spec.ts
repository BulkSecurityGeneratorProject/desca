/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { MainDatabaseDetailComponent } from '../../../../../../main/webapp/app/entities/main-database/main-database-detail.component';
import { MainDatabaseService } from '../../../../../../main/webapp/app/entities/main-database/main-database.service';
import { MainDatabase } from '../../../../../../main/webapp/app/entities/main-database/main-database.model';

describe('Component Tests', () => {

    describe('MainDatabase Management Detail Component', () => {
        let comp: MainDatabaseDetailComponent;
        let fixture: ComponentFixture<MainDatabaseDetailComponent>;
        let service: MainDatabaseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MainDatabaseDetailComponent],
                providers: [
                    MainDatabaseService
                ]
            })
            .overrideTemplate(MainDatabaseDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MainDatabaseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MainDatabaseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MainDatabase(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.mainDatabase).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
